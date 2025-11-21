# Contributing to Smart Healthcare Management System

First off, thank you for considering contributing to the Smart Healthcare Management System! It's people like you that make this project better for everyone.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing Requirements](#testing-requirements)
- [Documentation](#documentation)

## Code of Conduct

This project and everyone participating in it is governed by my commitment to fostering an open and welcoming environment. I pledge to:

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include:

- **Clear descriptive title**
- **Steps to reproduce** the issue
- **Expected behavior** vs actual behavior
- **Environment details** (OS, Java version, GlassFish version)
- **Stack traces or error messages**
- **Screenshots** if applicable

**Template for Bug Reports:**

```markdown
**Description:**
A clear description of the bug.

**Steps to Reproduce:**
1. Go to '...'
2. Click on '...'
3. See error

**Expected Behavior:**
What you expected to happen.

**Actual Behavior:**
What actually happened.

**Environment:**
- OS: [e.g., Ubuntu 22.04]
- Java: [e.g., OpenJDK 17]
- GlassFish: [e.g., 7.0.0]
- Browser: [e.g., Chrome 120]

**Additional Context:**
Any other relevant information.
```

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Clear descriptive title**
- **Detailed description** of the proposed feature
- **Use cases** explaining why this would be useful
- **Possible implementation** approach (optional)
- **Mockups or diagrams** if applicable

### Code Contributions

We actively welcome your pull requests:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes
4. Add tests for your changes
5. Ensure all tests pass (`ant test`)
6. Commit your changes (see commit guidelines below)
7. Push to the branch (`git push origin feature/AmazingFeature`)
8. Open a Pull Request

## Development Setup

### Prerequisites

- Java JDK 11 or 17
- Apache Ant 1.10+
- GlassFish 7.x
- Apache Derby 10.15+
- Git

### Initial Setup

```bash
# 1. Fork and clone the repository
git clone https://github.com/YOUR_USERNAME/HealthcareSystem.git
cd HealthcareSystem

# 2. Set up environment variables
cp scripts/.env.example scripts/.env
# Edit .env with your local paths

# 3. Build the project
ant clean compile

# 4. Start Derby database
./scripts/start-derby.sh

# 5. Seed the database
./scripts/seed-db.sh

# 6. Deploy to GlassFish
./scripts/run-dev.sh
```

### Running in Development Mode

```bash
# Watch mode (auto-redeploy on changes)
./scripts/run-dev.sh --watch

# Run tests
ant test

# Run specific test
ant test -Dtest.class=UserServiceTest
```

## Coding Standards

### Java Code Style

We follow standard Java conventions with these specifics:

#### Naming Conventions

- **Classes**: PascalCase (e.g., `UserService`, `AppointmentRepository`)
- **Methods**: camelCase (e.g., `findPatientById`, `createAppointment`)
- **Variables**: camelCase (e.g., `patientList`, `userId`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`, `DEFAULT_PAGE_SIZE`)
- **Packages**: lowercase (e.g., `com.healthcare.entity`)

#### Code Formatting

```java
// Use 4 spaces for indentation (NO tabs)
// Opening brace on same line
public class PatientService {
    
    // Blank line after class declaration
    private static final int MAX_PATIENTS = 1000;
    
    @Inject
    private PatientRepository patientRepository;
    
    // Blank line between methods
    public Patient findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return patientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Patient not found"));
    }
    
    // Use meaningful variable names
    public List<Patient> searchPatients(String query, int pageSize, int pageNumber) {
        validatePaginationParams(pageSize, pageNumber);
        return patientRepository.search(query, pageSize, pageNumber);
    }
}
```

#### Documentation

- **All public classes** must have JavaDoc
- **All public methods** must have JavaDoc with @param and @return
- **Complex private methods** should have explanatory comments

```java
/**
 * Service class for managing patient-related operations.
 * Provides CRUD operations and business logic for patient entities.
 * 
 * @author Healthcare System Team
 * @version 1.0
 * @since 2025-01-01
 */
@Stateless
public class PatientService {
    
    /**
     * Finds a patient by their unique identifier.
     * 
     * @param id the unique identifier of the patient
     * @return the patient entity
     * @throws NotFoundException if patient with given ID doesn't exist
     * @throws IllegalArgumentException if id is null
     */
    public Patient findById(Long id) {
        // Implementation
    }
}
```

### EJB Best Practices

- Use `@Stateless` for services without conversational state
- Use `@Singleton` for application-wide services (e.g., AnalyticsService)
- Avoid using `@Stateful` unless absolutely necessary
- Always inject dependencies via `@Inject` or `@EJB`
- Use transaction management annotations (`@TransactionAttribute`)

```java
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AppointmentService {
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Inject
    private NotificationService notificationService;
    
    // Methods here
}
```

### JPA Best Practices

- Use named queries for common operations
- Avoid N+1 query problems with fetch joins
- Use DTOs to transfer data to presentation layer
- Always use proper cascading and fetch types

```java
@Entity
@Table(name = "appointments")
@NamedQueries({
    @NamedQuery(
        name = "Appointment.findByPatient",
        query = "SELECT a FROM Appointment a WHERE a.patient.id = :patientId"
    )
})
public class Appointment {
    // Entity definition
}
```

### REST API Standards

- Use proper HTTP methods (GET, POST, PUT, DELETE)
- Return appropriate status codes
- Use DTOs for request/response bodies
- Implement proper error handling

```java
@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentResource {
    
    @GET
    public Response getAllAppointments() {
        // Return 200 OK with list
    }
    
    @POST
    public Response createAppointment(AppointmentDTO dto) {
        // Return 201 CREATED with Location header
    }
    
    @GET
    @Path("/{id}")
    public Response getAppointment(@PathParam("id") Long id) {
        // Return 200 OK or 404 NOT FOUND
    }
}
```

### Checkstyle Compliance

We use Checkstyle to enforce code quality. Run before committing:

```bash
ant checkstyle

# Fix common issues automatically
ant checkstyle-fix
```

Configuration: `tools/checkstyle.xml`

## Commit Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/) specification.

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Code style changes (formatting, missing semi-colons, etc.)
- **refactor**: Code refactoring without changing functionality
- **perf**: Performance improvements
- **test**: Adding or updating tests
- **build**: Changes to build system or dependencies
- **ci**: Changes to CI configuration files
- **chore**: Other changes that don't modify src or test files

### Examples

```bash
# Feature
git commit -m "feat(appointments): add appointment cancellation feature"

# Bug fix
git commit -m "fix(auth): resolve session timeout issue"

# Documentation
git commit -m "docs(api): update REST API documentation"

# Multiple lines
git commit -m "feat(notifications): implement email notifications

- Add JavaMail configuration
- Create NotificationMDB for async processing
- Add email templates

Closes #123"
```

### Scope Guidelines

Use these scopes:
- `entity` - JPA entities
- `service` - EJB services
- `rest` - JAX-RS resources
- `ui` - JSP pages and servlets
- `security` - Security components
- `config` - Configuration files
- `test` - Test files
- `build` - Build system
- `docs` - Documentation

## Pull Request Process

### Before Submitting

1. **Ensure your code builds successfully**
   ```bash
   ant clean compile
   ```

2. **Run all tests**
   ```bash
   ant test
   ```

3. **Check code style**
   ```bash
   ant checkstyle
   ```

4. **Update documentation** if needed

5. **Add tests** for new features

### PR Title Format

Use the same format as commit messages:
```
feat(appointments): add appointment cancellation feature
```

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
Describe the tests you ran and how to reproduce

## Checklist
- [ ] My code follows the project's code style
- [ ] I have performed a self-review
- [ ] I have commented my code where necessary
- [ ] I have updated the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix/feature works
- [ ] New and existing unit tests pass locally
- [ ] Any dependent changes have been merged

## Related Issues
Closes #(issue number)

## Screenshots (if applicable)
```

### Review Process

1. At least **one approval** required from maintainers
2. All CI checks must pass
3. No merge conflicts
4. Code coverage should not decrease
5. Documentation must be updated if API changes

### After Approval

- Maintainers will merge your PR
- Your contribution will be acknowledged in release notes
- Delete your feature branch after merge

## Testing Requirements

### Unit Tests

- **Minimum 80% code coverage** for new code
- Test all public methods in service classes
- Use mocks for external dependencies

```java
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    
    @Mock
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientService patientService;
    
    @Test
    void testFindById_Success() {
        // Arrange
        Patient expected = new Patient();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(expected));
        
        // Act
        Patient actual = patientService.findById(1L);
        
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
```

### Integration Tests

- Test REST endpoints with real HTTP calls
- Test JMS message processing
- Test database transactions

### Running Tests

```bash
# All tests
ant test

# Unit tests only
ant test-unit

# Integration tests only
ant test-integration

# Specific test class
ant test -Dtest.class=PatientServiceTest

# With coverage report
ant test-coverage
```

## Documentation

### Code Documentation

- JavaDoc for all public classes and methods
- Inline comments for complex logic
- README for each module if necessary

### API Documentation

- Update `docs/API.md` for REST endpoint changes
- Include request/response examples
- Document all error codes

### Architecture Documentation

Update relevant docs when making architectural changes:
- `docs/ARCHITECTURE.md` - Component diagrams
- `docs/DEPLOYMENT.md` - Deployment procedures
- `docs/SECURITY.md` - Security considerations

## Development Priorities

### High Priority
- Bug fixes
- Security vulnerabilities
- Performance issues
- Critical features

### Medium Priority
- Feature enhancements
- Refactoring
- Test coverage improvements

### Low Priority
- Code style improvements
- Documentation updates
- Minor UI tweaks

## Questions?

- Open a [GitHub Discussion](https://github.com/dennis-mmachoene/HealthcareSystem/discussions)
- Check existing [Issues](https://github.com/dennis-mmachoene/HealthcareSystem/issues)
- Review [Documentation](docs/)

## Thank You!

Your contributions make this project better. We appreciate your time and effort!

---

**Happy Coding!**