-- Database indices for performance optimization

-- ============================================
-- USERS Table Indices
-- ============================================
CREATE INDEX idx_users_email ON USERS(email);
CREATE INDEX idx_users_role ON USERS(role);
CREATE INDEX idx_users_active ON USERS(is_active);
CREATE INDEX idx_users_created_at ON USERS(created_at);

-- ============================================
-- PATIENTS Table Indices
-- ============================================
CREATE INDEX idx_patients_user_id ON PATIENTS(user_id);
CREATE INDEX idx_patients_blood_group ON PATIENTS(blood_group);
CREATE INDEX idx_patients_city ON PATIENTS(city);
CREATE INDEX idx_patients_state ON PATIENTS(state);

-- ============================================
-- DOCTORS Table Indices
-- ============================================
CREATE INDEX idx_doctors_user_id ON DOCTORS(user_id);
CREATE INDEX idx_doctors_specialization ON DOCTORS(specialization);
CREATE INDEX idx_doctors_license ON DOCTORS(license_number);
CREATE INDEX idx_doctors_approval_status ON DOCTORS(approval_status);
CREATE INDEX idx_doctors_availability ON DOCTORS(availability_status);

-- ============================================
-- APPOINTMENTS Table Indices
-- ============================================
CREATE INDEX idx_appointments_patient_id ON APPOINTMENTS(patient_id);
CREATE INDEX idx_appointments_doctor_id ON APPOINTMENTS(doctor_id);
CREATE INDEX idx_appointments_date ON APPOINTMENTS(appointment_date);
CREATE INDEX idx_appointments_status ON APPOINTMENTS(status);
CREATE INDEX idx_appointments_date_time ON APPOINTMENTS(appointment_date, appointment_time);
CREATE INDEX idx_appointments_created_at ON APPOINTMENTS(created_at);

-- Composite index for doctor's schedule
CREATE INDEX idx_appointments_doctor_date ON APPOINTMENTS(doctor_id, appointment_date);

-- Composite index for patient's appointments
CREATE INDEX idx_appointments_patient_date ON APPOINTMENTS(patient_id, appointment_date);

-- ============================================
-- MEDICAL_RECORDS Table Indices
-- ============================================
CREATE INDEX idx_medical_records_patient_id ON MEDICAL_RECORDS(patient_id);
CREATE INDEX idx_medical_records_doctor_id ON MEDICAL_RECORDS(doctor_id);
CREATE INDEX idx_medical_records_appointment_id ON MEDICAL_RECORDS(appointment_id);
CREATE INDEX idx_medical_records_date ON MEDICAL_RECORDS(record_date);
CREATE INDEX idx_medical_records_created_at ON MEDICAL_RECORDS(created_at);

-- ============================================
-- PRESCRIPTIONS Table Indices
-- ============================================
CREATE INDEX idx_prescriptions_patient_id ON PRESCRIPTIONS(patient_id);
CREATE INDEX idx_prescriptions_doctor_id ON PRESCRIPTIONS(doctor_id);
CREATE INDEX idx_prescriptions_appointment_id ON PRESCRIPTIONS(appointment_id);
CREATE INDEX idx_prescriptions_medical_record_id ON PRESCRIPTIONS(medical_record_id);
CREATE INDEX idx_prescriptions_prescribed_date ON PRESCRIPTIONS(prescribed_date);
CREATE INDEX idx_prescriptions_is_active ON PRESCRIPTIONS(is_active);
CREATE INDEX idx_prescriptions_medication ON PRESCRIPTIONS(medication_name);

-- ============================================
-- NOTIFICATIONS Table Indices
-- ============================================
CREATE INDEX idx_notifications_user_id ON NOTIFICATIONS(user_id);
CREATE INDEX idx_notifications_type ON NOTIFICATIONS(type);
CREATE INDEX idx_notifications_is_read ON NOTIFICATIONS(is_read);
CREATE INDEX idx_notifications_created_at ON NOTIFICATIONS(created_at);
CREATE INDEX idx_notifications_reference ON NOTIFICATIONS(reference_type, reference_id);

-- Composite index for unread notifications
CREATE INDEX idx_notifications_user_unread ON NOTIFICATIONS(user_id, is_read);

-- ============================================
-- AUDIT_LOG Table Indices
-- ============================================
CREATE INDEX idx_audit_log_user_id ON AUDIT_LOG(user_id);
CREATE INDEX idx_audit_log_action ON AUDIT_LOG(action);
CREATE INDEX idx_audit_log_entity ON AUDIT_LOG(entity_type, entity_id);
CREATE INDEX idx_audit_log_timestamp ON AUDIT_LOG(timestamp);

COMMIT;