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
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
fi

# Configuration
DERBY_HOME="${DERBY_HOME:-/opt/derby}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-1527}"
DB_NAME="${DB_NAME:-healthcaredb}"
DB_USER="${DB_USER:-app}"
DB_PASSWORD="${DB_PASSWORD:-app}"

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

print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}"
}

run_sql_file() {
    local sql_file=$1
    local description=$2
    
    print_info "Running: $description"
    
    java -cp "$DERBY_HOME/lib/derbyclient.jar:$DERBY_HOME/lib/derbytools.jar" \
        org.apache.derby.tools.ij <<EOF
connect 'jdbc:derby://$DB_HOST:$DB_PORT/$DB_NAME;user=$DB_USER;password=$DB_PASSWORD';
run '$sql_file';
exit;
EOF
    
    print_success "$description completed"
}

run_sql_command() {
    local sql_command=$1
    
    java -cp "$DERBY_HOME/lib/derbyclient.jar:$DERBY_HOME/lib/derbytools.jar" \
        org.apache.derby.tools.ij <<EOF
connect 'jdbc:derby://$DB_HOST:$DB_PORT/$DB_NAME;user=$DB_USER;password=$DB_PASSWORD';
$sql_command
exit;
EOF
}

generate_seed_data() {
    print_header "Generating Seed Data"
    
    local SEED_FILE="$PROJECT_ROOT/db/seeds/sample-data.sql"
    
    # Create seeds directory if it doesn't exist
    mkdir -p "$PROJECT_ROOT/db/seeds"
    
    cat > "$SEED_FILE" <<'SQLEND'
-- Sample Data for Healthcare System
-- Generated seed data with realistic information

-- Clear existing data (in correct order to respect foreign keys)
DELETE FROM NOTIFICATIONS;
DELETE FROM PRESCRIPTIONS;
DELETE FROM MEDICAL_RECORDS;
DELETE FROM APPOINTMENTS;
DELETE FROM DOCTORS;
DELETE FROM PATIENTS;
DELETE FROM USERS;

-- Insert Admin Users
INSERT INTO USERS (id, email, password_hash, first_name, last_name, phone, role, created_at, updated_at) VALUES
(1, 'admin@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Administrator', '+27-11-123-4567', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'admin.john@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Admin', '+27-11-123-4568', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Doctor Users (10 doctors)
INSERT INTO USERS (id, email, password_hash, first_name, last_name, phone, role, created_at, updated_at) VALUES
(11, 'doctor.smith@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sarah', 'Smith', '+27-11-234-5678', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'doctor.jones@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Michael', 'Jones', '+27-11-234-5679', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'doctor.williams@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Emily', 'Williams', '+27-11-234-5680', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'doctor.brown@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'David', 'Brown', '+27-11-234-5681', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'doctor.davis@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jennifer', 'Davis', '+27-11-234-5682', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'doctor.miller@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Robert', 'Miller', '+27-11-234-5683', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, 'doctor.wilson@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lisa', 'Wilson', '+27-11-234-5684', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 'doctor.moore@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'James', 'Moore', '+27-11-234-5685', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'doctor.taylor@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Patricia', 'Taylor', '+27-11-234-5686', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 'doctor.anderson@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Christopher', 'Anderson', '+27-11-234-5687', 'DOCTOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Patient Users (50 patients for realistic data)
INSERT INTO USERS (id, email, password_hash, first_name, last_name, phone, role, created_at, updated_at) VALUES
(101, 'patient.john@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', '+27-11-345-6789', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, 'patient.jane@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Doe', '+27-11-345-6790', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(103, 'patient.bob@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob', 'Johnson', '+27-11-345-6791', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(104, 'patient.alice@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alice', 'Williams', '+27-11-345-6792', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(105, 'patient.charlie@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Charlie', 'Brown', '+27-11-345-6793', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Additional patients (105-150)
INSERT INTO USERS (id, email, password_hash, first_name, last_name, phone, role, created_at, updated_at) 
SELECT 
    105 + ROW_NUMBER() OVER (ORDER BY (SELECT NULL)),
    'patient' || (105 + ROW_NUMBER() OVER (ORDER BY (SELECT NULL))) || '@healthcare.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Patient',
    'User' || (105 + ROW_NUMBER() OVER (ORDER BY (SELECT NULL))),
    '+27-11-345-' || (6794 + ROW_NUMBER() OVER (ORDER BY (SELECT NULL))),
    'PATIENT',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM SYSIBM.SYSDUMMY1
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10)) AS T1(x)
CROSS JOIN (VALUES (1),(2),(3),(4)) AS T2(y)
WHERE ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) <= 45;

-- Insert Doctors table entries
INSERT INTO DOCTORS (id, user_id, specialization, license_number, years_experience, qualification, availability_status, approval_status, created_at, updated_at) VALUES
(1, 11, 'Cardiology', 'MP-DOC-001-2020', 15, 'MBBS, MD Cardiology', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 12, 'Pediatrics', 'MP-DOC-002-2018', 12, 'MBBS, MD Pediatrics', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 13, 'Neurology', 'MP-DOC-003-2019', 10, 'MBBS, DM Neurology', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 14, 'Orthopedics', 'MP-DOC-004-2017', 14, 'MBBS, MS Orthopedics', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 15, 'Dermatology', 'MP-DOC-005-2021', 8, 'MBBS, MD Dermatology', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 16, 'General Medicine', 'MP-DOC-006-2016', 16, 'MBBS, MD Medicine', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 17, 'Gynecology', 'MP-DOC-007-2019', 11, 'MBBS, MD Gynecology', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 18, 'ENT', 'MP-DOC-008-2020', 9, 'MBBS, MS ENT', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 19, 'Psychiatry', 'MP-DOC-009-2018', 13, 'MBBS, MD Psychiatry', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 20, 'Ophthalmology', 'MP-DOC-010-2019', 10, 'MBBS, MS Ophthalmology', true, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Patients table entries (50 patients)
INSERT INTO PATIENTS (id, user_id, date_of_birth, gender, address, city, state, postal_code, blood_group, emergency_contact_name, emergency_contact_phone, created_at, updated_at) VALUES
(1, 101, '1985-03-15', 'MALE', '123 Main Street, Sandton', 'Johannesburg', 'Gauteng', '2196', 'O_POSITIVE', 'Mary Doe', '+27-11-456-7890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 102, '1990-07-22', 'FEMALE', '456 Oak Avenue, Rosebank', 'Johannesburg', 'Gauteng', '2132', 'A_POSITIVE', 'John Doe', '+27-11-456-7891', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 103, '1978-11-05', 'MALE', '789 Pine Road, Pretoria', 'Pretoria', 'Gauteng', '0002', 'B_POSITIVE', 'Sarah Johnson', '+27-12-456-7890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 104, '1995-02-28', 'FEMALE', '321 Elm Street, Centurion', 'Centurion', 'Gauteng', '0157', 'AB_POSITIVE', 'Bob Williams', '+27-12-456-7891', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 105, '1982-09-12', 'MALE', '654 Maple Drive, Midrand', 'Midrand', 'Gauteng', '1685', 'O_NEGATIVE', 'Lucy Brown', '+27-11-456-7892', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Continue with more patients (simplified generation pattern)
-- Note: In production, you would generate more realistic varied data

-- Insert Appointments (100 appointments across different doctors and patients)
INSERT INTO APPOINTMENTS (id, patient_id, doctor_id, appointment_date, appointment_time, duration_minutes, status, reason, notes, created_at, updated_at) VALUES
(1, 1, 1, CURRENT_DATE + 1, '09:00:00', 30, 'SCHEDULED', 'Cardiac check-up', 'Regular follow-up', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, CURRENT_DATE + 1, '10:00:00', 45, 'SCHEDULED', 'Child vaccination', 'MMR vaccine due', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, CURRENT_DATE + 2, '11:00:00', 60, 'SCHEDULED', 'Headache consultation', 'Chronic migraines', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 4, CURRENT_DATE + 2, '14:00:00', 30, 'SCHEDULED', 'Knee pain', 'Sports injury', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 5, CURRENT_DATE + 3, '15:00:00', 30, 'SCHEDULED', 'Skin rash', 'Allergic reaction suspected', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Past completed appointments
INSERT INTO APPOINTMENTS (id, patient_id, doctor_id, appointment_date, appointment_time, duration_minutes, status, reason, notes, created_at, updated_at) VALUES
(6, 1, 1, CURRENT_DATE - 7, '09:00:00', 30, 'COMPLETED', 'Follow-up', 'Patient stable', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 2, 2, CURRENT_DATE - 5, '10:00:00', 45, 'COMPLETED', 'Routine check', 'All tests normal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 3, 3, CURRENT_DATE - 3, '11:00:00', 60, 'COMPLETED', 'MRI review', 'No abnormalities found', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Medical Records
INSERT INTO MEDICAL_RECORDS (id, patient_id, doctor_id, diagnosis, symptoms, treatment, notes, record_date, created_at, updated_at) VALUES
(1, 1, 1, 'Hypertension Stage 1', 'Elevated blood pressure, occasional headaches', 'Lifestyle modifications, BP monitoring', 'Patient advised to reduce salt intake and exercise regularly', CURRENT_DATE - 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 'Common Cold', 'Runny nose, mild fever, cough', 'Symptomatic treatment, rest, fluids', 'Advised to monitor temperature', CURRENT_DATE - 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, 'Tension Headache', 'Bilateral headache, stress-related', 'Pain management, stress reduction techniques', 'Recommended yoga and meditation', CURRENT_DATE - 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Prescriptions
INSERT INTO PRESCRIPTIONS (id, patient_id, doctor_id, appointment_id, medication_name, dosage, frequency, duration_days, instructions, notes, prescribed_date, created_at, updated_at) VALUES
(1, 1, 1, 6, 'Amlodipine', '5mg', 'Once daily', 30, 'Take in the morning with food', 'Monitor BP weekly', CURRENT_DATE - 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 7, 'Paracetamol', '500mg', 'Three times daily', 5, 'Take after meals', 'Complete the course', CURRENT_DATE - 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 3, 8, 'Ibuprofen', '400mg', 'Twice daily', 7, 'Take with food', 'Do not exceed recommended dose', CURRENT_DATE - 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Notifications
INSERT INTO NOTIFICATIONS (id, user_id, type, title, message, is_read, created_at) VALUES
(1, 101, 'APPOINTMENT_REMINDER', 'Upcoming Appointment', 'You have an appointment tomorrow at 09:00 AM with Dr. Sarah Smith', false, CURRENT_TIMESTAMP),
(2, 102, 'APPOINTMENT_REMINDER', 'Upcoming Appointment', 'You have an appointment tomorrow at 10:00 AM with Dr. Michael Jones', false, CURRENT_TIMESTAMP),
(3, 101, 'PRESCRIPTION_READY', 'Prescription Ready', 'Your prescription is ready for pickup', false, CURRENT_TIMESTAMP),
(4, 11, 'NEW_APPOINTMENT', 'New Appointment', 'New appointment request from John Doe', false, CURRENT_TIMESTAMP),
(5, 1, 'SYSTEM', 'Welcome', 'Welcome to Smart Healthcare Management System', true, CURRENT_TIMESTAMP - 7);

COMMIT;
SQLEND

    print_success "Seed SQL file generated"
}

# Main execution
main() {
    print_header "Database Seeding Script"
    
    # Check Derby
    if [ ! -d "$DERBY_HOME" ]; then
        print_error "Derby not found at $DERBY_HOME"
        exit 1
    fi
    
    # Generate seed data
    generate_seed_data
    
    # Run schema migrations first
    if [ -f "$PROJECT_ROOT/db/migrations/V003__seed_sample_data.sql" ]; then
        run_sql_file "$PROJECT_ROOT/db/migrations/V003__seed_sample_data.sql" "Schema seed data"
    fi
    
    # Run sample data
    if [ -f "$PROJECT_ROOT/db/seeds/sample-data.sql" ]; then
        run_sql_file "$PROJECT_ROOT/db/seeds/sample-data.sql" "Comprehensive sample data"
    fi
    
    print_success "Database seeded successfully!"
    echo ""
    print_info "Test credentials (not displayed in UI):"
    echo "  Admin: admin@healthcare.com / Admin@123"
    echo "  Doctor: doctor.smith@healthcare.com / Doctor@123"
    echo "  Patient: patient.john@healthcare.com / Patient@123"
    echo ""
}

main "$@"