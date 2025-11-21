-- Initial seed data for development and testing
-- Password for all users: bcrypt hash of "Admin@123", "Doctor@123", "Patient@123"

-- Note: These are test accounts. Production should use proper password hashing.
-- BCrypt hash for "Admin@123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Insert System Administrator
INSERT INTO USERS (email, password_hash, first_name, last_name, phone, role, is_active, is_verified) VALUES
('admin@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Administrator', '+27-11-123-4567', 'ADMIN', TRUE, TRUE);

-- Insert Sample Doctor User
INSERT INTO USERS (email, password_hash, first_name, last_name, phone, role, is_active, is_verified) VALUES
('doctor.smith@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sarah', 'Smith', '+27-11-234-5678', 'DOCTOR', TRUE, TRUE);

-- Insert Sample Patient User
INSERT INTO USERS (email, password_hash, first_name, last_name, phone, role, is_active, is_verified) VALUES
('patient.john@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', '+27-11-345-6789', 'PATIENT', TRUE, TRUE);

-- Insert Doctor record for Dr. Smith (user_id will be 2 based on auto-increment)
INSERT INTO DOCTORS (user_id, specialization, license_number, years_experience, qualification, availability_status, approval_status) VALUES
(2, 'Cardiology', 'MP-DOC-001-2020', 15, 'MBBS, MD Cardiology', TRUE, 'APPROVED');

-- Insert Patient record for John Doe (user_id will be 3)
INSERT INTO PATIENTS (user_id, date_of_birth, gender, address, city, state, postal_code, blood_group, emergency_contact_name, emergency_contact_phone) VALUES
(3, '1985-03-15', 'MALE', '123 Main Street, Sandton', 'Johannesburg', 'Gauteng', '2196', 'O_POSITIVE', 'Mary Doe', '+27-11-456-7890');

COMMIT;