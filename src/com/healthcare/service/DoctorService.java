package com.healthcare.service;

import com.healthcare.entity.ApprovalStatus;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.User;
import com.healthcare.entity.UserRole;
import com.healthcare.exception.NotFoundException;
import com.healthcare.exception.ValidationException;
import com.healthcare.repository.DoctorRepository;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Doctor-related business operations.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DoctorService {
    
    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Inject
    private UserService userService;
    
    @Inject
    private NotificationService notificationService;
    
    /**
     * Find doctor by ID
     */
    public Doctor findById(Long id) {
        LOGGER.info("Finding doctor by ID: " + id);
        return doctorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + id));
    }
    
    /**
     * Find doctor by user ID
     */
    public Doctor findByUserId(Long userId) {
        LOGGER.info("Finding doctor by user ID: " + userId);
        return doctorRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Doctor not found for user ID: " + userId));
    }
    
    /**
     * Find all doctors
     */
    public List<Doctor> findAll() {
        LOGGER.info("Finding all doctors");
        return doctorRepository.findAll();
    }
    
    /**
     * Find doctors by specialization
     */
    public List<Doctor> findBySpecialization(String specialization) {
        LOGGER.info("Finding doctors by specialization: " + specialization);
        return doctorRepository.findBySpecialization(specialization);
    }
    
    /**
     * Find available doctors
     */
    public List<Doctor> findAvailable() {
        LOGGER.info("Finding available doctors");
        return doctorRepository.findAvailable();
    }
    
    /**
     * Find doctors pending approval
     */
    public List<Doctor> findPendingApproval() {
        LOGGER.info("Finding doctors pending approval");
        return doctorRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }
    
    /**
     * Apply to become a doctor (registration)
     */
    public Doctor applyAsDoctor(Doctor doctor, String email, String password,
                                String firstName, String lastName, String phone) {
        LOGGER.info("Doctor application received");
        
        // Validate doctor
        validateDoctor(doctor);
        
        // Check if license number already exists
        if (doctorRepository.findByLicenseNumber(doctor.getLicenseNumber()).isPresent()) {
            throw new ValidationException("License number already exists: " + doctor.getLicenseNumber());
        }
        
        // Create user account
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setRole(UserRole.DOCTOR);
        user.setIsActive(false); // Inactive until approved
        user.setIsVerified(false);
        
        User savedUser = userService.createUser(user, password);
        
        // Associate doctor with user
        doctor.setUser(savedUser);
        doctor.setApprovalStatus(ApprovalStatus.PENDING);
        
        // Save doctor
        Doctor savedDoctor = doctorRepository.save(doctor);
        LOGGER.info("Doctor application submitted successfully with ID: " + savedDoctor.getId());
        
        // TODO: Notify admins about new doctor application
        
        return savedDoctor;
    }
    
    /**
     * Approve doctor application
     */
    public void approveDoctor(Long doctorId, Long approverId) {
        LOGGER.info("Approving doctor ID: " + doctorId + " by admin ID: " + approverId);
        
        Doctor doctor = findById(doctorId);
        User approver = userService.findById(approverId);
        
        // Verify approver is admin
        if (approver.getRole() != UserRole.ADMIN) {
            throw new ValidationException("Only admins can approve doctor applications");
        }
        
        // Approve doctor
        doctor.approve(approver);
        doctorRepository.update(doctor);
        
        // Activate user account
        userService.activateUser(doctor.getUser().getId());
        
        // Send notification to doctor
        notificationService.sendDoctorApprovalNotification(doctor);
        
        LOGGER.info("Doctor approved successfully");
    }
    
    /**
     * Reject doctor application
     */
    public void rejectDoctor(Long doctorId, Long rejecterId) {
        LOGGER.info("Rejecting doctor ID: " + doctorId + " by admin ID: " + rejecterId);
        
        Doctor doctor = findById(doctorId);
        User rejecter = userService.findById(rejecterId);
        
        // Verify rejecter is admin
        if (rejecter.getRole() != UserRole.ADMIN) {
            throw new ValidationException("Only admins can reject doctor applications");
        }
        
        // Reject doctor
        doctor.reject(rejecter);
        doctorRepository.update(doctor);
        
        // TODO: Send rejection notification to doctor
        
        LOGGER.info("Doctor rejected successfully");
    }
    
    /**
     * Update doctor profile
     */
    public Doctor updateDoctor(Doctor doctor) {
        LOGGER.info("Updating doctor with ID: " + doctor.getId());
        
        // Verify doctor exists
        Doctor existingDoctor = findById(doctor.getId());
        
        // Check license number uniqueness if changed
        if (!existingDoctor.getLicenseNumber().equals(doctor.getLicenseNumber())) {
            if (doctorRepository.findByLicenseNumber(doctor.getLicenseNumber()).isPresent()) {
                throw new ValidationException("License number already exists: " + doctor.getLicenseNumber());
            }
        }
        
        // Update fields
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setLicenseNumber(doctor.getLicenseNumber());
        existingDoctor.setYearsExperience(doctor.getYearsExperience());
        existingDoctor.setQualification(doctor.getQualification());
        existingDoctor.setConsultationFee(doctor.getConsultationFee());
        existingDoctor.setBio(doctor.getBio());
        
        Doctor updatedDoctor = doctorRepository.update(existingDoctor);
        LOGGER.info("Doctor updated successfully");
        
        return updatedDoctor;
    }
    
    /**
     * Update doctor availability
     */
    public void updateAvailability(Long doctorId, boolean available) {
        LOGGER.info("Updating availability for doctor ID: " + doctorId + " to: " + available);
        
        Doctor doctor = findById(doctorId);
        doctor.setAvailabilityStatus(available);
        doctorRepository.update(doctor);
        
        LOGGER.info("Doctor availability updated successfully");
    }
    
    /**
     * Delete doctor
     */
    public void deleteDoctor(Long id) {
        LOGGER.info("Deleting doctor with ID: " + id);
        
        // Verify doctor exists
        findById(id);
        
        doctorRepository.delete(id);
        LOGGER.info("Doctor deleted successfully");
    }
    
    /**
     * Get distinct specializations
     */
    public List<String> getSpecializations() {
        return doctorRepository.getDistinctSpecializations();
    }
    
    /**
     * Count doctors by approval status
     */
    public Long countByStatus(ApprovalStatus status) {
        return doctorRepository.countByStatus(status);
    }
    
    /**
     * Find doctors with pagination
     */
    public List<Doctor> findAll(int pageNumber, int pageSize) {
        LOGGER.info("Finding doctors - Page: " + pageNumber + ", Size: " + pageSize);
        return doctorRepository.findAll(pageNumber, pageSize);
    }
    
    /**
     * Count total doctors
     */
    public Long count() {
        return doctorRepository.count();
    }
    
    // Private helper methods
    
    private void validateDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new ValidationException("Doctor cannot be null");
        }
        
        if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
            throw new ValidationException("Specialization is required");
        }
        
        if (doctor.getLicenseNumber() == null || doctor.getLicenseNumber().trim().isEmpty()) {
            throw new ValidationException("License number is required");
        }
        
        // Validate license number format (XX-XXX-XXX-XXXX)
        if (!doctor.getLicenseNumber().matches("^[A-Z]{2}-[A-Z]{3}-[0-9]{3}-[0-9]{4}$")) {
            throw new ValidationException("Invalid license number format. Expected: XX-XXX-XXX-XXXX");
        }
    }
}