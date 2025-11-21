package com.healthcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Prescription entity representing medication prescriptions.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@Entity
@Table(name = "PRESCRIPTIONS")
@NamedQueries({
    @NamedQuery(
        name = "Prescription.findAll",
        query = "SELECT p FROM Prescription p ORDER BY p.prescribedDate DESC"
    ),
    @NamedQuery(
        name = "Prescription.findByPatient",
        query = "SELECT p FROM Prescription p WHERE p.patient.id = :patientId ORDER BY p.prescribedDate DESC"
    ),
    @NamedQuery(
        name = "Prescription.findByDoctor",
        query = "SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId ORDER BY p.prescribedDate DESC"
    ),
    @NamedQuery(
        name = "Prescription.findByAppointment",
        query = "SELECT p FROM Prescription p WHERE p.appointment.id = :appointmentId"
    ),
    @NamedQuery(
        name = "Prescription.findActive",
        query = "SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.isActive = true ORDER BY p.prescribedDate DESC"
    ),
    @NamedQuery(
        name = "Prescription.findByMedication",
        query = "SELECT p FROM Prescription p WHERE LOWER(p.medicationName) LIKE LOWER(:medicationName) ORDER BY p.prescribedDate DESC"
    )
})
public class Prescription implements Serializable {
    
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;
    
    @NotNull(message = "Medication name is required")
    @Size(min = 2, max = 500, message = "Medication name must be between 2 and 500 characters")
    @Column(name = "medication_name", nullable = false, length = 500)
    private String medicationName;
    
    @NotNull(message = "Dosage is required")
    @Size(min = 1, max = 200, message = "Dosage must be between 1 and 200 characters")
    @Column(name = "dosage", nullable = false, length = 200)
    private String dosage;
    
    @NotNull(message = "Frequency is required")
    @Size(min = 1, max = 200, message = "Frequency must be between 1 and 200 characters")
    @Column(name = "frequency", nullable = false, length = 200)
    private String frequency;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    @Max(value = 365, message = "Duration cannot exceed 365 days")
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private Integer quantity;
    
    @Lob
    @Column(name = "instructions")
    private String instructions;
    
    @Lob
    @Column(name = "notes")
    private String notes;
    
    @NotNull(message = "Prescribed date is required")
    @PastOrPresent(message = "Prescribed date cannot be in the future")
    @Column(name = "prescribed_date", nullable = false)
    private LocalDate prescribedDate;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Prescription() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.prescribedDate = LocalDate.now();
    }
    
    public Prescription(Patient patient, Doctor doctor, String medicationName, String dosage, String frequency, Integer durationDays) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        // Calculate end date if start date is set
        if (startDate != null && durationDays != null) {
            endDate = startDate.plusDays(durationDays);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        
        // Recalculate end date if start date or duration changed
        if (startDate != null && durationDays != null) {
            endDate = startDate.plusDays(durationDays);
        }
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
    
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
    
    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
    
    public String getMedicationName() {
        return medicationName;
    }
    
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public Integer getDurationDays() {
        return durationDays;
    }
    
    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDate getPrescribedDate() {
        return prescribedDate;
    }
    
    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate = prescribedDate;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
    
    // Utility methods
    public boolean isExpired() {
        if (endDate != null) {
            return LocalDate.now().isAfter(endDate);
        }
        return false;
    }
    
    public int getRemainingDays() {
        if (endDate != null && !isExpired()) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        }
        return 0;
    }
    
    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getUser().getFullName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getUser().getFullName() : "null") +
                ", medicationName='" + medicationName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", prescribedDate=" + prescribedDate +
                '}';
    }
}