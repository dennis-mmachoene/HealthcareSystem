-- Initial database schema for Smart Healthcare Management System
-- Apache Derby SQL Syntax

-- Drop tables if they exist (in correct order to respect foreign keys)
DROP TABLE IF EXISTS NOTIFICATIONS;
DROP TABLE IF EXISTS PRESCRIPTIONS;
DROP TABLE IF EXISTS MEDICAL_RECORDS;
DROP TABLE IF EXISTS APPOINTMENTS;
DROP TABLE IF EXISTS DOCTORS;
DROP TABLE IF EXISTS PATIENTS;
DROP TABLE IF EXISTS USERS;

-- ============================================
-- USERS Table (Base user information)
-- ============================================
CREATE TABLE USERS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'DOCTOR', 'PATIENT')),
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- ============================================
-- PATIENTS Table (Patient-specific information)
-- ============================================
CREATE TABLE PATIENTS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id BIGINT NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    blood_group VARCHAR(10) CHECK (blood_group IN ('A_POSITIVE', 'A_NEGATIVE', 'B_POSITIVE', 'B_NEGATIVE', 'AB_POSITIVE', 'AB_NEGATIVE', 'O_POSITIVE', 'O_NEGATIVE')),
    emergency_contact_name VARCHAR(200),
    emergency_contact_phone VARCHAR(20),
    medical_history CLOB,
    allergies CLOB,
    current_medications CLOB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE
);

-- ============================================
-- DOCTORS Table (Doctor-specific information)
-- ============================================
CREATE TABLE DOCTORS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id BIGINT NOT NULL UNIQUE,
    specialization VARCHAR(200) NOT NULL,
    license_number VARCHAR(100) NOT NULL UNIQUE,
    years_experience INT DEFAULT 0,
    qualification VARCHAR(500),
    consultation_fee DECIMAL(10,2),
    availability_status BOOLEAN DEFAULT TRUE,
    approval_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (approval_status IN ('PENDING', 'APPROVED', 'REJECTED')),
    approved_by BIGINT,
    approved_at TIMESTAMP,
    bio CLOB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES USERS(id) ON DELETE SET NULL
);

-- ============================================
-- APPOINTMENTS Table
-- ============================================
CREATE TABLE APPOINTMENTS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    duration_minutes INT DEFAULT 30,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')),
    reason VARCHAR(500),
    notes CLOB,
    cancellation_reason VARCHAR(500),
    cancelled_by BIGINT,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES PATIENTS(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES DOCTORS(id) ON DELETE CASCADE,
    FOREIGN KEY (cancelled_by) REFERENCES USERS(id) ON DELETE SET NULL
);

-- ============================================
-- MEDICAL_RECORDS Table
-- ============================================
CREATE TABLE MEDICAL_RECORDS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_id BIGINT,
    diagnosis VARCHAR(1000),
    symptoms CLOB,
    treatment CLOB,
    test_results CLOB,
    notes CLOB,
    attachments VARCHAR(1000),
    record_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES PATIENTS(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES DOCTORS(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES APPOINTMENTS(id) ON DELETE SET NULL
);

-- ============================================
-- PRESCRIPTIONS Table
-- ============================================
CREATE TABLE PRESCRIPTIONS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_id BIGINT,
    medical_record_id BIGINT,
    medication_name VARCHAR(500) NOT NULL,
    dosage VARCHAR(200) NOT NULL,
    frequency VARCHAR(200) NOT NULL,
    duration_days INT NOT NULL,
    quantity INT,
    instructions CLOB,
    notes CLOB,
    prescribed_date DATE NOT NULL,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES PATIENTS(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES DOCTORS(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES APPOINTMENTS(id) ON DELETE SET NULL,
    FOREIGN KEY (medical_record_id) REFERENCES MEDICAL_RECORDS(id) ON DELETE SET NULL
);

-- ============================================
-- NOTIFICATIONS Table
-- ============================================
CREATE TABLE NOTIFICATIONS (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('APPOINTMENT_REMINDER', 'APPOINTMENT_CONFIRMATION', 'APPOINTMENT_CANCELLATION', 'PRESCRIPTION_READY', 'TEST_RESULTS_AVAILABLE', 'NEW_APPOINTMENT', 'SYSTEM', 'ALERT')),
    title VARCHAR(255) NOT NULL,
    message CLOB NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    reference_id BIGINT,
    reference_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE
);

-- ============================================
-- AUDIT_LOG Table (Optional - for tracking changes)
-- ============================================
CREATE TABLE AUDIT_LOG (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    old_value CLOB,
    new_value CLOB,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE SET NULL
);

COMMIT;