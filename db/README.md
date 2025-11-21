# Database Migrations and Seeds

This directory contains database schema migrations and seed data for the Smart Healthcare Management System.

## Directory Structure

```
db/
├── migrations/          # Schema migrations (versioned)
│   ├── V001__create_schema.sql
│   ├── V002__create_indices.sql
│   └── V003__seed_sample_data.sql
├── seeds/              # Sample data for development
│   └── sample-data.sql
└── README.md           # This file
```

## Database Schema

The application uses **Apache Derby** as the database. The schema includes:

- **USERS**: Base user information (admin, doctor, patient)
- **PATIENTS**: Patient-specific details
- **DOCTORS**: Doctor profiles and credentials
- **APPOINTMENTS**: Appointment scheduling
- **MEDICAL_RECORDS**: Patient medical history
- **PRESCRIPTIONS**: Medication prescriptions
- **NOTIFICATIONS**: System notifications
- **AUDIT_LOG**: Audit trail (optional)

## Running Migrations

### Using the Seed Script

```bash
./scripts/seed-db.sh
```

### Manual Migration

```bash
# Start Derby Network Server
cd $DERBY_HOME/bin
./startNetworkServer

# Run migrations using ij
java -jar $DERBY_HOME/lib/derbyrun.jar ij <<EOF
connect 'jdbc:derby://localhost:1527/healthcaredb;create=true';
run 'db/migrations/V001__create_schema.sql';
run 'db/migrations/V002__create_indices.sql';
run 'db/migrations/V003__seed_sample_data.sql';
exit;
EOF
```

## Test Credentials

After seeding, the following test accounts are available:

**Note**: These credentials are NOT displayed on the login page.

```
Administrator:
  Email: admin@healthcare.com
  Password: Admin@123

Doctor:
  Email: doctor.smith@healthcare.com
  Password: Doctor@123

Patient:
  Email: patient.john@healthcare.com
  Password: Patient@123
```

## Connection Details

```
Host: localhost
Port: 1527
Database: healthcaredb
JDBC URL: jdbc:derby://localhost:1527/healthcaredb
```

## Schema Versioning

We follow a versioning convention:

- `V001__description.sql`: Major schema changes
- `V002__description.sql`: Index creation
- `V003__description.sql`: Seed data

## Adding New Migrations

1. Create a new file: `V004__your_description.sql`
2. Write your SQL statements
3. Test locally before committing
4. Update this README with changes

## Rollback Strategy

Derby doesn't have built-in rollback for DDL. To rollback:

1. Keep backups of your database
2. Write reverse migration scripts
3. Test migrations in dev environment first

## Performance Considerations

- Indices are created in V002
- Use appropriate data types
- Avoid large CLOB fields when possible
- Regular vacuuming/compaction for production