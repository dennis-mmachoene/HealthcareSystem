#!/bin/bash
# scripts/docker/build-image.sh - Docker image build script

set -e

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
VERSION="${VERSION:-1.0.0}"
REGISTRY="${DOCKER_REGISTRY:-}"

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

# Build application first
print_info "Building application artifacts..."
cd "$PROJECT_ROOT"
ant clean package-war

# Build GlassFish image
print_info "Building GlassFish Docker image..."
docker build \
    -f infra/docker/Dockerfile.glassfish \
    -t healthcare-system:${VERSION} \
    -t healthcare-system:latest \
    .

print_success "GlassFish image built"

# Build Derby image
print_info "Building Derby Docker image..."
docker build \
    -f infra/docker/Dockerfile.derby \
    -t healthcare-derby:${VERSION} \
    -t healthcare-derby:latest \
    .

print_success "Derby image built"

# Tag for registry if specified
if [ -n "$REGISTRY" ]; then
    print_info "Tagging images for registry: $REGISTRY"
    
    docker tag healthcare-system:latest ${REGISTRY}/healthcare-system:${VERSION}
    docker tag healthcare-system:latest ${REGISTRY}/healthcare-system:latest
    docker tag healthcare-derby:latest ${REGISTRY}/healthcare-derby:${VERSION}
    docker tag healthcare-derby:latest ${REGISTRY}/healthcare-derby:latest
    
    print_success "Images tagged for registry"
fi

print_success "All Docker images built successfully!"
echo ""
echo "Images:"
echo "  healthcare-system:${VERSION}"
echo "  healthcare-derby:${VERSION}"