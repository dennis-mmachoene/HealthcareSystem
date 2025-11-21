package com.healthcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Appointment entity representing scheduled medical appointments.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@Entity
@Table(name = "APPOINTMENTS")
@NamedQueries({
    @NamedQuery(
        name = "Appointment.findAll",
        query = "SELECT a FROM Appointment a ORDER BY a.appointmentDate DESC, a.appointmentTime DESC"
    ),
    @NamedQuery(
        name = "Appointment.findByPatient",
        query = "SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC"
    ),
    @NamedQuery(
        name = "Appointment.findByDoctor",
        query = "SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId ORDER BY a.appointmentDate DESC"
    ),
    @NamedQuery(
        name = "Appointment.findByStatus",
        query = "SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.appointmentDate DESC"
    ),
    @NamedQuery(
        name = "Appointment.findByDate",
        query = "SELECT a FROM Appointment a WHERE a.appointmentDate = :date ORDER BY a.appointmentTime"
    ),
    @NamedQuery(
        name = "Appointment.findUpcoming",
        query = "SELECT a FROM Appointment a WHERE a.appointmentDate >= :today AND a.status = 'SCHEDULED' ORDER BY a.appointmentDate, a.appointmentTime"
    ),
    @NamedQuery(
        name = "Appointment.findDoctorSchedule",
        query = "SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date ORDER BY a.appointmentTime"
    )
})
public class Appointment implements Serializable {
    
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
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
    
    @NotNull(message = "Appointment time is required")
    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;
    
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 120, message = "Duration cannot exceed 120 minutes")
    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
    
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    @Column(name = "reason", length = 500)
    private String reason;
    
    @Lob
    @Column(name = "notes")
    private String notes;
    
    @Size(max = 500, message = "Cancellation reason must not exceed 500 characters")
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Appointment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Appointment(Patient patient, Doctor doctor, LocalDate appointmentDate, LocalTime appointmentTime) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
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
    
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }
    
    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    public User getCancelledBy() {
        return cancelledBy;
    }
    
    public void setCancelledBy(User cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
    
    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
    
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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
    public LocalDateTime getAppointmentDateTime() {
        if (appointmentDate != null && appointmentTime != null) {
            return LocalDateTime.of(appointmentDate, appointmentTime);
        }
        return null;
    }
    
    public boolean isPast() {
        LocalDateTime appointmentDateTime = getAppointmentDateTime();
        return appointmentDateTime != null && appointmentDateTime.isBefore(LocalDateTime.now());
    }
    
    public boolean isUpcoming() {
        LocalDateTime appointmentDateTime = getAppointmentDateTime();
        return appointmentDateTime != null && appointmentDateTime.isAfter(LocalDateTime.now());
    }
    
    public void cancel(User user, String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelledBy = user;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
    }
    
    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getUser().getFullName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getUser().getFullName() : "null") +
                ", date=" + appointmentDate +
                ", time=" + appointmentTime +
                ", status=" + status +
                '}';
    }
}