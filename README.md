# Smart Healthcare Management System

**A Jakarta EE 9/10 enterprise application for healthcare management**

[![Build Status](https://github.com/dennis-mmachoene/HealthcareSystem/workflows/CI/badge.svg)](https://github.com/dennis-mmachoene/HealthcareSystem/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Building the Application](#building-the-application)
- [Running Locally](#running-locally)
- [Running Tests](#running-tests)
- [Deployment](#deployment)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Smart Healthcare Management System is a comprehensive enterprise application built with Jakarta EE that provides:

- **Patient Management**: Registration, profile management, medical history
- **Doctor Management**: Application workflow, approval system, scheduling
- **Appointment System**: Booking, cancellation, status tracking
- **Prescription Management**: Create, view, and manage prescriptions
- **Real-time Notifications**: JMS-based notification system
- **Analytics Dashboard**: System-wide analytics for administrators
- **RESTful API**: Complete REST API for mobile/SPA clients
- **Secure Authentication**: Role-based access control (ADMIN, DOCTOR, PATIENT)

## Features

### Patient Portal
- Self-registration and profile management
- Book appointments with available doctors
- View appointment history and status
- Access medical records and prescriptions
- Receive real-time notifications

### Doctor Portal
- Apply for doctor registration (requires admin approval)
- Manage patient appointments
- Create and manage prescriptions
- View patient medical records
- Dashboard with analytics

### Admin Portal
- Approve/reject doctor applications
- Manage users (patients, doctors, admins)
- System-wide analytics and reports
- Monitor notifications and system health

### Technical Features
- **JPA 2.2**: Entity persistence with Apache Derby
- **EJB 3.2**: Stateless and Singleton session beans
- **JAX-RS 2.1**: RESTful web services
- **JMS 2.0**: Asynchronous messaging with MDBs
- **JSP/Servlets**: Server-side rendering for web UI
- **Security**: Container-managed authentication and authorization
- **Responsive UI**: Bootstrap 5 with custom styling

## 🛠 Technology Stack

### Backend
- **Jakarta EE 9/10** (EJB, JPA, JAX-RS, JMS, Servlets)
- **GlassFish 7.x** - Application Server
- **Apache Derby** - Embedded/Network Database
- **Apache Ant** - Build System

### Frontend
- **JSP 2.3** with JSTL & EL
- **Bootstrap 5.3** - Responsive framework
- **Font Awesome 6.x** - Icons
- **jQuery 3.x** - DOM manipulation

### Development & Testing
- **JUnit 5** - Unit testing
- **Arquillian** - Integration testing
- **GitHub Actions** - CI/CD
- **Docker & Docker Compose** - Containerization

## Prerequisites

### Required Software
- **Java JDK 11 or 17** (Jakarta EE 9/10 compatible)
- **Apache Ant 1.10+**
- **GlassFish 7.x** or compatible Jakarta EE server
- **Apache Derby 10.15+**
- **Git** for version control

### Optional Tools
- **Docker & Docker Compose** (for containerized development)
- **Maven** (if you prefer Maven over Ant)
- **IntelliJ IDEA** or **Eclipse IDE** with Jakarta EE support

### Verify Installation

```bash
# Check Java version
java -version  # Should be 11 or 17

# Check Ant version
ant -version   # Should be 1.10+

# Check GlassFish installation
asadmin version
```

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/dennis-mmachoene/HealthcareSystem.git
cd HealthcareSystem
```

### 2. Configure Environment

```bash
# Copy and edit environment configuration
cp scripts/.env.example scripts/.env

# Edit the following variables:
# - GLASSFISH_HOME=/path/to/glassfish7
# - DERBY_HOME=/path/to/derby
# - DB_HOST=localhost
# - DB_PORT=1527
```

### 3. Build the Application

```bash
# Build both EJB and WAR modules
ant clean compile package-ejb package-war assemble

# Or use the build script
./scripts/build.sh
```

### 4. Start Database and Seed Data

```bash
# Start Apache Derby in network mode
./scripts/start-derby.sh

# Create schema and seed sample data
./scripts/seed-db.sh
```

### 5. Deploy to GlassFish

```bash
# Start GlassFish domain
./scripts/start-glassfish.sh

# Deploy the application
ant deploy-local

# Or use the dev script (starts everything)
./scripts/run-dev.sh
```

### 6. Access the Application

- **Web UI**: http://localhost:8080/healthcare
- **REST API**: http://localhost:8080/healthcare/api
- **Admin Console**: http://localhost:4848 (GlassFish admin:admin)

### 7. Test Users (Created by seed script)

**Note**: Demo credentials are NOT visible on the login page. Use these for testing:

```
Admin User:
  Email: admin@healthcare.com
  Password: Admin@123

Doctor User:
  Email: doctor.smith@healthcare.com
  Password: Doctor@123

Patient User:
  Email: patient.john@healthcare.com
  Password: Patient@123
```

## Project Structure

```
HealthcareSystem/
├── build/                          # Build artifacts (JAR, WAR, EAR)
├── src/com/healthcare/             # Java source code
│   ├── config/                     # Application configuration
│   ├── entity/                     # JPA entities
│   ├── dto/                        # Data Transfer Objects
│   ├── mapper/                     # Entity-DTO mappers
│   ├── repository/                 # Data access layer
│   ├── service/                    # Business logic (EJBs)
│   ├── rest/                       # JAX-RS resources
│   ├── servlet/                    # Servlets for UI
│   ├── mdb/                        # Message-Driven Beans
│   ├── filter/                     # Servlet filters
│   ├── interceptor/                # EJB/JAX-RS interceptors
│   ├── util/                       # Utility classes
│   ├── exception/                  # Custom exceptions
│   ├── security/                   # Security components
│   ├── jobs/                       # Scheduled jobs
│   └── test/                       # Unit & integration tests
├── web/                            # Web application root
│   ├── index.jsp                   # Landing page
│   ├── assets/                     # CSS, JS, images
│   ├── WEB-INF/                    # Protected resources
│   │   ├── web.xml                 # Deployment descriptor
│   │   ├── glassfish-resources.xml # JDBC pools, JMS config
│   │   └── jsp/                    # JSP pages
│   └── META-INF/
│       └── persistence.xml         # JPA configuration
├── db/                             # Database migrations & seeds
├── docs/                           # Documentation
├── scripts/                        # Build & deployment scripts
├── infra/                          # Docker & K8s configs
└── build.xml                       # Ant build file
```

## Building the Application

### Ant Targets

```bash
# Clean build artifacts
ant clean

# Compile EJB module
ant compile-ejb

# Compile WAR module
ant compile-war

# Run tests
ant test

# Package EJB JAR
ant package-ejb

# Package WAR file
ant package-war

# Assemble EAR (optional)
ant assemble

# Deploy to local GlassFish
ant deploy-local

# Complete build pipeline
ant clean compile test package-ejb package-war assemble
```

### Build Artifacts

After building, you'll find:
- `build/Smart_Healthcare_Management_System-ejb.jar` - EJB module
- `build/Smart_Healthcare_Management_System-war.war` - WAR module
- `build/Smart_Healthcare_Management_System.ear` - Full EAR (optional)

## 🏃 Running Locally

### Option 1: Traditional Deployment

```bash
# 1. Start Derby database
cd $DERBY_HOME/bin
./startNetworkServer

# 2. Start GlassFish
cd $GLASSFISH_HOME/bin
./asadmin start-domain domain1

# 3. Deploy application
ant deploy-local

# 4. Access at http://localhost:8080/healthcare
```

### Option 2: Using Helper Scripts

```bash
# All-in-one development script
./scripts/run-dev.sh

# This script will:
# - Start Derby database
# - Start GlassFish server
# - Build and deploy application
# - Run seed scripts
# - Tail server logs
```

### Option 3: Docker Compose

```bash
# Start all services (GlassFish, Derby, App)
docker-compose -f infra/docker/docker-compose.yml up -d

# View logs
docker-compose logs -f healthcare-app

# Stop all services
docker-compose down
```

## Running Tests

### Unit Tests

```bash
# Run all tests
ant test

# Run specific test class
ant test -Dtest.class=UserServiceTest

# Generate test reports
ant test-report
```

### Integration Tests

```bash
# Run integration tests (requires running server)
ant integration-test

# Run REST API tests
ant test-rest

# Run JMS integration tests
ant test-jms
```

### Test Coverage

```bash
# Generate JaCoCo coverage report
ant test-coverage

# Report will be in build/coverage/index.html
```

## Deployment

### GlassFish Deployment

See [DEPLOYMENT.md](docs/DEPLOYMENT.md) for detailed instructions.

**Quick deployment:**

```bash
# 1. Build the application
ant package-ejb package-war

# 2. Deploy via asadmin
asadmin deploy --force=true \
  --contextroot healthcare \
  build/Smart_Healthcare_Management_System-war.war

# 3. Verify deployment
asadmin list-applications
```

### Docker Deployment

```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.glassfish \
  -t healthcare-system:latest .

# Run container
docker run -d \
  -p 8080:8080 -p 4848:4848 \
  --name healthcare-app \
  healthcare-system:latest
```

### Kubernetes Deployment

See [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) for K8s manifests and Helm charts.

## API Documentation

### REST Endpoints

Complete API documentation is available in [docs/API.md](docs/API.md).

**Base URL**: `http://localhost:8080/healthcare/api`

**Key Endpoints**:

```
Authentication:
  POST   /api/auth/login
  POST   /api/auth/register
  POST   /api/auth/logout

Patients:
  GET    /api/patients
  GET    /api/patients/{id}
  POST   /api/patients
  PUT    /api/patients/{id}
  DELETE /api/patients/{id}

Doctors:
  GET    /api/doctors
  GET    /api/doctors/{id}
  POST   /api/doctors/apply
  PUT    /api/doctors/{id}/approve

Appointments:
  GET    /api/appointments
  POST   /api/appointments
  PUT    /api/appointments/{id}
  DELETE /api/appointments/{id}

Prescriptions:
  GET    /api/prescriptions
  POST   /api/prescriptions
  GET    /api/prescriptions/{id}

Notifications:
  GET    /api/notifications
  PUT    /api/notifications/{id}/read
```

### Example Request

```bash
# Login
curl -X POST http://localhost:8080/healthcare/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "patient.john@healthcare.com",
    "password": "Patient@123"
  }'

# Get all doctors
curl -X GET http://localhost:8080/healthcare/api/doctors \
  -H "Authorization: Bearer <token>"
```

## Security

### Authentication & Authorization

- **Container-managed security** using GlassFish JDBC realm
- **Three roles**: ADMIN, DOCTOR, PATIENT
- **Password hashing**: BCrypt with salt
- **JWT tokens** (optional) for REST API authentication

### Security Configuration

See [docs/SECURITY.md](docs/SECURITY.md) for complete security documentation.

**Key security features**:
- Session timeout: 30 minutes
- Password policy: Min 8 chars, 1 uppercase, 1 lowercase, 1 number
- HTTPS required in production
- CORS configured for REST API
- SQL injection prevention via JPA
- XSS protection via output encoding

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details on:
- Code style guidelines
- Branch naming conventions
- Pull request process
- Testing requirements

### Development Workflow

```bash
# 1. Fork and clone
git clone https://github.com/yourfork/HealthcareSystem.git

# 2. Create feature branch
git checkout -b feature/your-feature-name

# 3. Make changes and test
ant clean test

# 4. Commit with descriptive message
git commit -m "feat: add patient search functionality"

# 5. Push and create PR
git push origin feature/your-feature-name
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Documentation**: [docs/](docs/)
- **Issues**: [GitHub Issues](https://github.com/dennis-mmachoene/HealthcareSystem/issues)
- **Discussions**: [GitHub Discussions](https://github.com/dennis-mmachoene/HealthcareSystem/discussions)

## Acknowledgments

- Jakarta EE community
- GlassFish development team
- Bootstrap and Font Awesome contributors
- All project contributors

---

**Built with ❤️ for healthcare professionals and patients**