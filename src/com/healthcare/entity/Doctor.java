package com.healthcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Doctor entity representing medical professional information.
 * Contains doctor credentials, specialization, and approval status.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@Entity
@Table(name = "DOCTORS")
@NamedQueries({
    @NamedQuery(
        name = "Doctor.findAll",
        query = "SELECT d FROM Doctor d ORDER BY d.createdAt DESC"
    ),
    @NamedQuery(
        name = "Doctor.findByUserId",
        query = "SELECT d FROM Doctor d WHERE d.user.id = :userId"
    ),
    @NamedQuery(
        name = "Doctor.findBySpecialization",
        query = "SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.approvalStatus = 'APPROVED' AND d.availabilityStatus = true"
    ),
    @NamedQuery(
        name = "Doctor.findByApprovalStatus",
        query = "SELECT d FROM Doctor d WHERE d.approvalStatus = :status ORDER BY d.createdAt DESC"
    ),
    @NamedQuery(
        name = "Doctor.findAvailable",
        query = "SELECT d FROM Doctor d WHERE d.availabilityStatus = true AND d.approvalStatus = 'APPROVED'"
    ),
    @NamedQuery(
        name = "Doctor.findByLicenseNumber",
        query = "SELECT d FROM Doctor d WHERE d.licenseNumber = :licenseNumber"
    ),
    @NamedQuery(
        name = "Doctor.countByStatus",
        query = "SELECT COUNT(d) FROM Doctor d WHERE d.approvalStatus = :status"
    )
})
public class Doctor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull(message = "User is required")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @NotNull(message = "Specialization is required")
    @Size(min = 2, max = 200, message = "Specialization must be between 2 and 200 characters")
    @Column(name = "specialization", nullable = false, length = 200)
    private String specialization;
    
    @NotNull(message = "License number is required")
    @Pattern(regexp = "^[A-Z]{2}-[A-Z]{3}-[0-9]{3}-[0-9]{4}$", message = "Invalid license number format")
    @Column(name = "license_number", nullable = false, unique = true, length = 100)
    private String licenseNumber;
    
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 70, message = "Years of experience seems unrealistic")
    @Column(name = "years_experience")
    private Integer yearsExperience = 0;
    
    @Size(max = 500, message = "Qualification must not exceed 500 characters")
    @Column(name = "qualification", length = 500)
    private String qualification;
    
    @DecimalMin(value = "0.0", message = "Consultation fee cannot be negative")
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee;
    
    @Column(name = "availability_status")
    private Boolean availabilityStatus = true;
    
    @NotNull(message = "Approval status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Lob
    @Column(name = "bio")
    private String bio;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Doctor() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Doctor(User user, String specialization, String licenseNumber) {
        this();
        this.user = user;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public Integer getYearsExperience() {
        return yearsExperience;
    }
    
    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public BigDecimal getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(Boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
    
    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    
    public User getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
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
    public boolean isApproved() {
        return approvalStatus == ApprovalStatus.APPROVED;
    }
    
    public boolean isAvailable() {
        return availabilityStatus != null && availabilityStatus && isApproved();
    }
    
    public void approve(User approver) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }
    
    public void reject(User rejector) {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.approvedBy = rejector;
        this.approvedAt = LocalDateTime.now();
    }
    
    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) && Objects.equals(licenseNumber, doctor.licenseNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, licenseNumber);
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", specialization='" + specialization + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", approvalStatus=" + approvalStatus +
                '}';
    }
}