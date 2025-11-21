#!/bin/bash

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment variables
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
fi

# Default configuration
GLASSFISH_HOME="${GLASSFISH_HOME:-/opt/glassfish7}"
DERBY_HOME="${DERBY_HOME:-/opt/derby}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-1527}"
DB_NAME="${DB_NAME:-healthcaredb}"
GLASSFISH_PORT="${GLASSFISH_PORT:-8080}"
GLASSFISH_ADMIN_PORT="${GLASSFISH_ADMIN_PORT:-4848}"
GLASSFISH_DOMAIN="${GLASSFISH_DOMAIN:-domain1}"

# PID files
DERBY_PID_FILE="$PROJECT_ROOT/.derby.pid"
GLASSFISH_PID_FILE="$PROJECT_ROOT/.glassfish.pid"

# Functions
print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        return 0  # Port is in use
    else
        return 1  # Port is free
    fi
}

wait_for_service() {
    local host=$1
    local port=$2
    local service_name=$3
    local max_attempts=30
    local attempt=0
    
    print_info "Waiting for $service_name to be ready..."
    
    while [ $attempt -lt $max_attempts ]; do
        if check_port $port; then
            print_success "$service_name is ready on $host:$port"
            return 0
        fi
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    echo ""
    print_error "$service_name failed to start within $((max_attempts * 2)) seconds"
    return 1
}

start_derby() {
    print_header "Starting Apache Derby Database"
    
    if [ ! -d "$DERBY_HOME" ]; then
        print_error "Derby not found at $DERBY_HOME"
        print_info "Please install Derby or set DERBY_HOME in .env"
        exit 1
    fi
    
    # Check if Derby is already running
    if check_port $DB_PORT; then
        print_warning "Derby already running on port $DB_PORT"
        return 0
    fi
    
    print_info "Starting Derby Network Server..."
    
    cd "$DERBY_HOME/bin"
    
    # Start Derby in background
    if [ -f "./startNetworkServer" ]; then
        ./startNetworkServer -h $DB_HOST -p $DB_PORT > "$PROJECT_ROOT/derby.log" 2>&1 &
        DERBY_PID=$!
        echo $DERBY_PID > "$DERBY_PID_FILE"
        print_success "Derby started (PID: $DERBY_PID)"
    else
        print_error "startNetworkServer script not found"
        exit 1
    fi
    
    # Wait for Derby to be ready
    wait_for_service $DB_HOST $DB_PORT "Derby"
    
    echo ""
}

stop_derby() {
    print_info "Stopping Derby..."
    
    if [ -f "$DERBY_PID_FILE" ]; then
        DERBY_PID=$(cat "$DERBY_PID_FILE")
        if ps -p $DERBY_PID > /dev/null 2>&1; then
            kill $DERBY_PID
            rm "$DERBY_PID_FILE"
            print_success "Derby stopped"
        else
            print_warning "Derby process not found"
            rm "$DERBY_PID_FILE"
        fi
    else
        print_warning "Derby PID file not found"
    fi
}

create_database() {
    print_header "Creating Database Schema"
    
    # Check if database exists
    print_info "Checking if database exists..."
    
    # Run schema creation script
    if [ -f "$PROJECT_ROOT/db/migrations/V001__create_schema.sql" ]; then
        print_info "Running schema creation script..."
        
        # Use ij (Derby's interactive SQL tool) to create database
        cd "$DERBY_HOME/bin"
        
        # Create connection string
        java -jar "$DERBY_HOME/lib/derbyrun.jar" ij <<EOF > "$PROJECT_ROOT/db-setup.log" 2>&1
connect 'jdbc:derby://$DB_HOST:$DB_PORT/$DB_NAME;create=true';
run '$PROJECT_ROOT/db/migrations/V001__create_schema.sql';
run '$PROJECT_ROOT/db/migrations/V002__create_indices.sql';
exit;
EOF
        
        print_success "Database schema created"
    else
        print_warning "Schema creation script not found"
    fi
    
    echo ""
}

seed_database() {
    print_header "Seeding Database"
    
    if [ -f "$SCRIPT_DIR/seed-db.sh" ]; then
        print_info "Running seed script..."
        bash "$SCRIPT_DIR/seed-db.sh"
        print_success "Database seeded with sample data"
    else
        print_warning "Seed script not found at $SCRIPT_DIR/seed-db.sh"
    fi
    
    echo ""
}

start_glassfish() {
    print_header "Starting GlassFish Application Server"
    
    if [ ! -d "$GLASSFISH_HOME" ]; then
        print_error "GlassFish not found at $GLASSFISH_HOME"
        print_info "Please install GlassFish or set GLASSFISH_HOME in .env"
        exit 1
    fi
    
    # Check if GlassFish is already running
    if check_port $GLASSFISH_ADMIN_PORT; then
        print_warning "GlassFish already running on port $GLASSFISH_ADMIN_PORT"
        return 0
    fi
    
    print_info "Starting GlassFish domain: $GLASSFISH_DOMAIN..."
    
    cd "$GLASSFISH_HOME/bin"
    ./asadmin start-domain $GLASSFISH_DOMAIN
    
    # Wait for GlassFish to be ready
    wait_for_service localhost $GLASSFISH_ADMIN_PORT "GlassFish Admin"
    wait_for_service localhost $GLASSFISH_PORT "GlassFish HTTP"
    
    print_success "GlassFish started successfully"
    echo ""
}

stop_glassfish() {
    print_info "Stopping GlassFish..."
    
    if [ -d "$GLASSFISH_HOME" ]; then
        cd "$GLASSFISH_HOME/bin"
        ./asadmin stop-domain $GLASSFISH_DOMAIN 2>/dev/null || true
        print_success "GlassFish stopped"
    fi
}

build_application() {
    print_header "Building Application"
    
    cd "$PROJECT_ROOT"
    
    print_info "Running Ant build..."
    bash "$SCRIPT_DIR/build.sh" --skip-docs --skip-checkstyle
    
    print_success "Application built successfully"
    echo ""
}

deploy_application() {
    print_header "Deploying Application"
    
    cd "$PROJECT_ROOT"
    
    print_info "Deploying to GlassFish..."
    ant deploy-local
    
    print_success "Application deployed successfully"
    echo ""
}

show_info() {
    print_header "Development Environment Ready"
    
    echo -e "${GREEN}Application URLs:${NC}"
    echo -e "  Web UI:        ${BLUE}http://localhost:$GLASSFISH_PORT/healthcare${NC}"
    echo -e "  REST API:      ${BLUE}http://localhost:$GLASSFISH_PORT/healthcare/api${NC}"
    echo -e "  Admin Console: ${BLUE}http://localhost:$GLASSFISH_ADMIN_PORT${NC}"
    echo ""
    
    echo -e "${GREEN}Database Connection:${NC}"
    echo -e "  Host:     $DB_HOST"
    echo -e "  Port:     $DB_PORT"
    echo -e "  Database: $DB_NAME"
    echo -e "  JDBC URL: jdbc:derby://$DB_HOST:$DB_PORT/$DB_NAME"
    echo ""
    
    echo -e "${GREEN}Log Files:${NC}"
    echo -e "  Derby:     $PROJECT_ROOT/derby.log"
    echo -e "  GlassFish: $GLASSFISH_HOME/glassfish/domains/$GLASSFISH_DOMAIN/logs/server.log"
    echo ""
    
    echo -e "${YELLOW}Test Credentials (not shown in UI):${NC}"
    echo -e "  Admin:   admin@healthcare.com / Admin@123"
    echo -e "  Doctor:  doctor.smith@healthcare.com / Doctor@123"
    echo -e "  Patient: patient.john@healthcare.com / Patient@123"
    echo ""
    
    print_info "Press Ctrl+C to stop all services"
}

tail_logs() {
    print_info "Tailing GlassFish logs (Ctrl+C to stop)..."
    tail -f "$GLASSFISH_HOME/glassfish/domains/$GLASSFISH_DOMAIN/logs/server.log"
}

cleanup() {
    echo ""
    print_header "Shutting Down Services"
    
    stop_glassfish
    stop_derby
    
    print_success "All services stopped"
    exit 0
}

# Trap SIGINT and SIGTERM
trap cleanup SIGINT SIGTERM

# Parse arguments
SKIP_BUILD=false
SKIP_SEED=false
WATCH_LOGS=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-build)
            SKIP_BUILD=true
            shift
            ;;
        --skip-seed)
            SKIP_SEED=true
            shift
            ;;
        --watch)
            WATCH_LOGS=true
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --skip-build   Skip building the application"
            echo "  --skip-seed    Skip seeding the database"
            echo "  --watch        Tail GlassFish logs after startup"
            echo "  --help, -h     Show this help message"
            echo ""
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Main execution
main() {
    print_header "Smart Healthcare System - Development Environment"
    echo ""
    
    # Start services
    start_derby
    create_database
    
    if [ "$SKIP_SEED" = false ]; then
        seed_database
    fi
    
    start_glassfish
    
    # Build and deploy
    if [ "$SKIP_BUILD" = false ]; then
        build_application
        deploy_application
    else
        print_warning "Skipping build as requested"
    fi
    
    # Show info
    show_info
    
    # Watch logs if requested
    if [ "$WATCH_LOGS" = true ]; then
        tail_logs
    else
        print_info "Run 'tail -f $GLASSFISH_HOME/glassfish/domains/$GLASSFISH_DOMAIN/logs/server.log' to view logs"
        echo ""
        print_info "Services are running. Press Ctrl+C to stop."
        
        # Keep script running
        while true; do
            sleep 1
        done
    fi
}

# Run main
main "$@"