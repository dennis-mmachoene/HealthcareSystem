#!/bin/bash

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Load environment
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
fi

# Configuration
GLASSFISH_HOME="${GLASSFISH_HOME:-/opt/glassfish7}"
GLASSFISH_DOMAIN="${GLASSFISH_DOMAIN:-domain1}"
GLASSFISH_ADMIN_PORT="${GLASSFISH_ADMIN_PORT:-4848}"
GLASSFISH_PORT="${GLASSFISH_PORT:-8080}"
GLASSFISH_ADMIN_USER="${GLASSFISH_ADMIN_USER:-admin}"

# Functions
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

check_glassfish() {
    if [ ! -d "$GLASSFISH_HOME" ]; then
        print_error "GlassFish not found at $GLASSFISH_HOME"
        exit 1
    fi
}

is_running() {
    if lsof -Pi :$GLASSFISH_ADMIN_PORT -sTCP:LISTEN -t >/dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

start_domain() {
    check_glassfish
    
    if is_running; then
        print_info "GlassFish is already running"
        return 0
    fi
    
    print_info "Starting GlassFish domain: $GLASSFISH_DOMAIN..."
    
    cd "$GLASSFISH_HOME/bin"
    ./asadmin start-domain $GLASSFISH_DOMAIN
    
    print_success "GlassFish started successfully"
    print_info "Admin Console: http://localhost:$GLASSFISH_ADMIN_PORT"
    print_info "HTTP Listener: http://localhost:$GLASSFISH_PORT"
}

stop_domain() {
    check_glassfish
    
    if ! is_running; then
        print_info "GlassFish is not running"
        return 0
    fi
    
    print_info "Stopping GlassFish domain: $GLASSFISH_DOMAIN..."
    
    cd "$GLASSFISH_HOME/bin"
    ./asadmin stop-domain $GLASSFISH_DOMAIN
    
    print_success "GlassFish stopped successfully"
}

restart_domain() {
    stop_domain
    sleep 2
    start_domain
}

status_domain() {
    check_glassfish
    
    cd "$GLASSFISH_HOME/bin"
    ./asadmin list-domains
    
    if is_running; then
        print_success "GlassFish is running"
    else
        print_info "GlassFish is not running"
    fi
}

# Main
case "${1:-start}" in
    start)
        start_domain
        ;;
    stop)
        stop_domain
        ;;
    restart)
        restart_domain
        ;;
    status)
        status_domain
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        exit 1
        ;;
esac