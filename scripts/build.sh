#!/bin/bash

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment variables if .env exists
if [ -f "$SCRIPT_DIR/.env" ]; then
    echo -e "${BLUE}Loading environment variables from .env${NC}"
    source "$SCRIPT_DIR/.env"
fi

# Default values
ANT_HOME="${ANT_HOME:-/usr/share/ant}"
GLASSFISH_HOME="${GLASSFISH_HOME:-/opt/glassfish7}"
DERBY_HOME="${DERBY_HOME:-/opt/derby}"

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

check_prerequisites() {
    print_header "Checking Prerequisites"
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed"
        exit 1
    fi
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_success "Java version: $JAVA_VERSION"
    
    # Check Ant
    if ! command -v ant &> /dev/null; then
        print_error "Apache Ant is not installed"
        exit 1
    fi
    ANT_VERSION=$(ant -version 2>&1 | grep -oP 'Apache Ant\(TM\) version \K[0-9.]+')
    print_success "Ant version: $ANT_VERSION"
    
    # Check GlassFish
    if [ ! -d "$GLASSFISH_HOME" ]; then
        print_warning "GlassFish not found at $GLASSFISH_HOME"
        print_info "Set GLASSFISH_HOME environment variable or update .env file"
    else
        print_success "GlassFish home: $GLASSFISH_HOME"
    fi
    
    # Check Derby
    if [ ! -d "$DERBY_HOME" ]; then
        print_warning "Derby not found at $DERBY_HOME"
        print_info "Set DERBY_HOME environment variable or update .env file"
    else
        print_success "Derby home: $DERBY_HOME"
    fi
    
    echo ""
}

clean_build() {
    print_header "Cleaning Build Artifacts"
    cd "$PROJECT_ROOT"
    ant clean
    print_success "Build artifacts cleaned"
    echo ""
}

compile_project() {
    print_header "Compiling Project"
    cd "$PROJECT_ROOT"
    
    print_info "Compiling EJB module..."
    ant compile-ejb
    print_success "EJB module compiled"
    
    print_info "Compiling WAR module..."
    ant compile-war
    print_success "WAR module compiled"
    
    echo ""
}

run_tests() {
    print_header "Running Tests"
    cd "$PROJECT_ROOT"
    
    print_info "Running unit tests..."
    ant test || {
        print_warning "Some tests failed, but continuing..."
    }
    
    print_success "Tests completed"
    echo ""
}

run_checkstyle() {
    print_header "Running Code Quality Checks"
    cd "$PROJECT_ROOT"
    
    print_info "Running Checkstyle..."
    ant checkstyle || {
        print_warning "Code style issues found"
    }
    
    print_success "Code quality checks completed"
    echo ""
}

package_artifacts() {
    print_header "Packaging Artifacts"
    cd "$PROJECT_ROOT"
    
    print_info "Packaging EJB JAR..."
    ant package-ejb
    print_success "EJB JAR created: build/Smart_Healthcare_Management_System-ejb.jar"
    
    print_info "Packaging WAR..."
    ant package-war
    print_success "WAR created: build/Smart_Healthcare_Management_System-war.war"
    
    print_info "Assembling EAR..."
    ant assemble
    print_success "EAR created: build/Smart_Healthcare_Management_System.ear"
    
    echo ""
}

generate_docs() {
    print_header "Generating Documentation"
    cd "$PROJECT_ROOT"
    
    print_info "Generating Javadoc..."
    ant javadoc
    print_success "Javadoc generated: build/docs/javadoc"
    
    echo ""
}

show_summary() {
    print_header "Build Summary"
    
    cd "$PROJECT_ROOT"
    
    if [ -f "build/Smart_Healthcare_Management_System-ejb.jar" ]; then
        JAR_SIZE=$(du -h build/Smart_Healthcare_Management_System-ejb.jar | cut -f1)
        print_success "EJB JAR: $JAR_SIZE"
    fi
    
    if [ -f "build/Smart_Healthcare_Management_System-war.war" ]; then
        WAR_SIZE=$(du -h build/Smart_Healthcare_Management_System-war.war | cut -f1)
        print_success "WAR: $WAR_SIZE"
    fi
    
    if [ -f "build/Smart_Healthcare_Management_System.ear" ]; then
        EAR_SIZE=$(du -h build/Smart_Healthcare_Management_System.ear | cut -f1)
        print_success "EAR: $EAR_SIZE"
    fi
    
    echo ""
    print_info "Build artifacts are ready in the 'build/' directory"
    print_info "Deploy with: ant deploy-local"
    print_info "Or run: ./scripts/run-dev.sh"
    echo ""
}

# Parse command line arguments
SKIP_TESTS=false
SKIP_CHECKSTYLE=false
SKIP_DOCS=false
CLEAN_FIRST=true

while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        --skip-checkstyle)
            SKIP_CHECKSTYLE=true
            shift
            ;;
        --skip-docs)
            SKIP_DOCS=true
            shift
            ;;
        --no-clean)
            CLEAN_FIRST=false
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --skip-tests       Skip running tests"
            echo "  --skip-checkstyle  Skip code style checks"
            echo "  --skip-docs        Skip documentation generation"
            echo "  --no-clean         Don't clean before building"
            echo "  --help, -h         Show this help message"
            echo ""
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Main build process
main() {
    print_header "Smart Healthcare Management System - Build"
    echo ""
    
    # Check prerequisites
    check_prerequisites
    
    # Clean build
    if [ "$CLEAN_FIRST" = true ]; then
        clean_build
    fi
    
    # Compile
    compile_project
    
    # Run tests
    if [ "$SKIP_TESTS" = false ]; then
        run_tests
    else
        print_warning "Skipping tests as requested"
    fi
    
    # Run checkstyle
    if [ "$SKIP_CHECKSTYLE" = false ]; then
        run_checkstyle
    else
        print_warning "Skipping checkstyle as requested"
    fi
    
    # Package
    package_artifacts
    
    # Generate docs
    if [ "$SKIP_DOCS" = false ]; then
        generate_docs
    else
        print_warning "Skipping documentation generation as requested"
    fi
    
    # Show summary
    show_summary
    
    print_success "Build completed successfully!"
}

# Run main function
main "$@"