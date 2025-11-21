package com.healthcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MedicalRecord entity representing patient medical history and diagnoses.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@Entity
@Table(name = "MEDICAL_RECORDS")
@NamedQueries({
    @NamedQuery(
        name = "MedicalRecord.findAll",
        query = "SELECT m FROM MedicalRecord m ORDER BY m.recordDate DESC"
    ),
    @NamedQuery(
        name = "MedicalRecord.findByPatient",
        query = "SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId ORDER BY m.recordDate DESC"
    ),
    @NamedQuery(
        name = "MedicalRecord.findByDoctor",
        query = "SELECT m FROM MedicalRecord m WHERE m.doctor.id = :doctorId ORDER BY m.recordDate DESC"
    ),
    @NamedQuery(
        name = "MedicalRecord.findByAppointment",
        query = "SELECT m FROM MedicalRecord m WHERE m.appointment.id = :appointmentId"
    ),
    @NamedQuery(
        name = "MedicalRecord.findByDateRange",
        query = "SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId AND m.recordDate BETWEEN :startDate AND :endDate ORDER BY m.recordDate DESC"
    )
})
public class MedicalRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    @Size(max = 1000, message = "Diagnosis must not exceed 1000 characters")
    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;
    
    @Lob
    @Column(name = "symptoms")
    private String symptoms;
    
    @Lob
    @Column(name = "treatment")
    private String treatment;
    
    @Lob
    @Column(name = "test_results")
    private String testResults;
    
    @Lob
    @Column(name = "notes")
    private String notes;
    
    @Size(max = 1000, message = "Attachments must not exceed 1000 characters")
    @Column(name = "attachments", length = 1000)
    private String attachments;
    
    @NotNull(message = "Record date is required")
    @PastOrPresent(message = "Record date cannot be in the future")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public MedicalRecord() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.recordDate = LocalDate.now();
    }
    
    public MedicalRecord(Patient patient, Doctor doctor, String diagnosis) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getSymptoms() {
        return symptoms;
    }
    
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    
    public String getTreatment() {
        return treatment;
    }
    
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
    
    public String getTestResults() {
        return testResults;
    }
    
    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getAttachments() {
        return attachments;
    }
    
    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
    
    public LocalDate getRecordDate() {
        return recordDate;
    }
    
    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getUser().getFullName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getUser().getFullName() : "null") +
                ", diagnosis='" + diagnosis + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}