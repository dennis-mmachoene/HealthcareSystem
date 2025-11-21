package com.healthcare.service;

import com.healthcare.entity.Patient;
import com.healthcare.entity.User;
import com.healthcare.entity.UserRole;
import com.healthcare.exception.NotFoundException;
import com.healthcare.exception.ValidationException;
import com.healthcare.repository.PatientRepository;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Patient-related business operations.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PatientService {
    
    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private UserService userService;
    
    /**
     * Find patient by ID
     */
    public Patient findById(Long id) {
        LOGGER.info("Finding patient by ID: " + id);
        return patientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + id));
    }
    
    /**
     * Find patient by user ID
     */
    public Patient findByUserId(Long userId) {
        LOGGER.info("Finding patient by user ID: " + userId);
        return patientRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Patient not found for user ID: " + userId));
    }
    
    /**
     * Find all patients
     */
    public List<Patient> findAll() {
        LOGGER.info("Finding all patients");
        return patientRepository.findAll();
    }
    
    /**
     * Create new patient
     */
    public Patient createPatient(Patient patient, String email, String password, 
                                 String firstName, String lastName, String phone) {
        LOGGER.info("Creating new patient");
        
        // Validate patient
        validatePatient(patient);
        
        // Create user account
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setRole(UserRole.PATIENT);
        user.setIsActive(true);
        user.setIsVerified(true);
        
        User savedUser = userService.createUser(user, password);
        
        // Associate patient with user
        patient.setUser(savedUser);
        
        // Save patient
        Patient savedPatient = patientRepository.save(patient);
        LOGGER.info("Patient created successfully with ID: " + savedPatient.getId());
        
        return savedPatient;
    }
    
    /**
     * Update existing patient
     */
    public Patient updatePatient(Patient patient) {
        LOGGER.info("Updating patient with ID: " + patient.getId());
        
        // Verify patient exists
        Patient existingPatient = findById(patient.getId());
        
        // Update fields
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setCity(patient.getCity());
        existingPatient.setState(patient.getState());
        existingPatient.setPostalCode(patient.getPostalCode());
        existingPatient.setBloodGroup(patient.getBloodGroup());
        existingPatient.setEmergencyContactName(patient.getEmergencyContactName());
        existingPatient.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        existingPatient.setMedicalHistory(patient.getMedicalHistory());
        existingPatient.setAllergies(patient.getAllergies());
        existingPatient.setCurrentMedications(patient.getCurrentMedications());
        
        Patient updatedPatient = patientRepository.update(existingPatient);
        LOGGER.info("Patient updated successfully");
        
        return updatedPatient;
    }
    
    /**
     * Delete patient
     */
    public void deletePatient(Long id) {
        LOGGER.info("Deleting patient with ID: " + id);
        
        // Verify patient exists
        Patient patient = findById(id);
        
        // Delete patient (cascade will handle user deletion)
        patientRepository.delete(id);
        LOGGER.info("Patient deleted successfully");
    }
    
    /**
     * Search patients by name
     */
    public List<Patient> searchByName(String searchTerm) {
        LOGGER.info("Searching patients by name: " + searchTerm);
        return patientRepository.searchByName(searchTerm);
    }
    
    /**
     * Find patients by city
     */
    public List<Patient> findByCity(String city) {
        LOGGER.info("Finding patients by city: " + city);
        return patientRepository.findByCity(city);
    }
    
    /**
     * Find patients with pagination
     */
    public List<Patient> findAll(int pageNumber, int pageSize) {
        LOGGER.info("Finding patients - Page: " + pageNumber + ", Size: " + pageSize);
        return patientRepository.findAll(pageNumber, pageSize);
    }
    
    /**
     * Count total patients
     */
    public Long count() {
        return patientRepository.count();
    }
    
    /**
     * Update patient medical history
     */
    public void updateMedicalHistory(Long patientId, String medicalHistory) {
        LOGGER.info("Updating medical history for patient ID: " + patientId);
        
        Patient patient = findById(patientId);
        patient.setMedicalHistory(medicalHistory);
        patientRepository.update(patient);
        
        LOGGER.info("Medical history updated successfully");
    }
    
    /**
     * Update patient allergies
     */
    public void updateAllergies(Long patientId, String allergies) {
        LOGGER.info("Updating allergies for patient ID: " + patientId);
        
        Patient patient = findById(patientId);
        patient.setAllergies(allergies);
        patientRepository.update(patient);
        
        LOGGER.info("Allergies updated successfully");
    }
    
    // Private helper methods
    
    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new ValidationException("Patient cannot be null");
        }
        
        if (patient.getDateOfBirth() == null) {
            throw new ValidationException("Date of birth is required");
        }
        
        if (patient.getGender() == null) {
            throw new ValidationException("Gender is required");
        }
    }
}