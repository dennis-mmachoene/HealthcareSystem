package com.healthcare.dto;

import com.healthcare.entity.ApprovalStatus;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Doctor entity.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public class DoctorDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private UserDTO user;
    private String specialization;
    private String licenseNumber;
    private Integer yearsExperience;
    private String qualification;
    private BigDecimal consultationFee;
    private Boolean availabilityStatus;
    private ApprovalStatus approvalStatus;
    private String bio;
    
    // Constructors
    public DoctorDTO() {
    }
    
    public DoctorDTO(Long id, UserDTO user, String specialization, String licenseNumber) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
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
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    @Override
    public String toString() {
        return "DoctorDTO{" +
                "id=" + id +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", specialization='" + specialization + '\'' +
                ", approvalStatus=" + approvalStatus +
                '}';
    }
}