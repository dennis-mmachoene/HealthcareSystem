package com.healthcare.service;

import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.entity.Prescription;
import com.healthcare.exception.NotFoundException;
import com.healthcare.exception.ValidationException;
import com.healthcare.repository.PrescriptionRepository;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Prescription-related business operations.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PrescriptionService {
    
    private static final Logger LOGGER = Logger.getLogger(PrescriptionService.class.getName());
    
    @Inject
    private PrescriptionRepository prescriptionRepository;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private NotificationService notificationService;
    
    /**
     * Find prescription by ID
     */
    public Prescription findById(Long id) {
        LOGGER.info("Finding prescription by ID: " + id);
        return prescriptionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Prescription not found with ID: " + id));
    }
    
    /**
     * Find all prescriptions
     */
    public List<Prescription> findAll() {
        LOGGER.info("Finding all prescriptions");
        return prescriptionRepository.findAll();
    }
    
    /**
     * Find prescriptions by patient
     */
    public List<Prescription> findByPatient(Long patientId) {
        LOGGER.info("Finding prescriptions for patient ID: " + patientId);
        return prescriptionRepository.findByPatient(patientId);
    }
    
    /**
     * Find prescriptions by doctor
     */
    public List<Prescription> findByDoctor(Long doctorId) {
        LOGGER.info("Finding prescriptions by doctor ID: " + doctorId);
        return prescriptionRepository.findByDoctor(doctorId);
    }
    
    /**
     * Find active prescriptions for patient
     */
    public List<Prescription> findActiveByPatient(Long patientId) {
        LOGGER.info("Finding active prescriptions for patient ID: " + patientId);
        return prescriptionRepository.findActive(patientId);
    }
    
    /**
     * Create new prescription
     */
    public Prescription createPrescription(Long patientId, Long doctorId, 
                                          String medicationName, String dosage, 
                                          String frequency, Integer durationDays,
                                          String instructions, String notes) {
        LOGGER.info("Creating new prescription");
        
        // Validate inputs
        validatePrescriptionData(medicationName, dosage, frequency, durationDays);
        
        // Get patient and doctor
        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.findById(doctorId);
        
        // Verify doctor is approved
        if (!doctor.isApproved()) {
            throw new ValidationException("Only approved doctors can prescribe medication");
        }
        
        // Create prescription
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setMedicationName(medicationName);
        prescription.setDosage(dosage);
        prescription.setFrequency(frequency);
        prescription.setDurationDays(durationDays);
        prescription.setInstructions(instructions);
        prescription.setNotes(notes);
        prescription.setPrescribedDate(LocalDate.now());
        prescription.setStartDate(LocalDate.now());
        prescription.setIsActive(true);
        
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        LOGGER.info("Prescription created successfully with ID: " + savedPrescription.getId());
        
        // Send notification to patient
        notificationService.sendPrescriptionNotification(savedPrescription);
        
        return savedPrescription;
    }
    
    /**
     * Update prescription
     */
    public Prescription updatePrescription(Prescription prescription) {
        LOGGER.info("Updating prescription ID: " + prescription.getId());
        
        // Verify prescription exists
        Prescription existingPrescription = findById(prescription.getId());
        
        // Update fields
        existingPrescription.setMedicationName(prescription.getMedicationName());
        existingPrescription.setDosage(prescription.getDosage());
        existingPrescription.setFrequency(prescription.getFrequency());
        existingPrescription.setDurationDays(prescription.getDurationDays());
        existingPrescription.setQuantity(prescription.getQuantity());
        existingPrescription.setInstructions(prescription.getInstructions());
        existingPrescription.setNotes(prescription.getNotes());
        existingPrescription.setIsActive(prescription.getIsActive());
        
        Prescription updatedPrescription = prescriptionRepository.update(existingPrescription);
        LOGGER.info("Prescription updated successfully");
        
        return updatedPrescription;
    }
    
    /**
     * Deactivate prescription
     */
    public void deactivatePrescription(Long prescriptionId) {
        LOGGER.info("Deactivating prescription ID: " + prescriptionId);
        
        Prescription prescription = findById(prescriptionId);
        prescription.setIsActive(false);
        prescriptionRepository.update(prescription);
        
        LOGGER.info("Prescription deactivated successfully");
    }
    
    /**
     * Delete prescription
     */
    public void deletePrescription(Long id) {
        LOGGER.info("Deleting prescription ID: " + id);
        
        // Verify prescription exists
        findById(id);
        
        prescriptionRepository.delete(id);
        LOGGER.info("Prescription deleted successfully");
    }
    
    /**
     * Find prescriptions with pagination
     */
    public List<Prescription> findAll(int pageNumber, int pageSize) {
        LOGGER.info("Finding prescriptions - Page: " + pageNumber + ", Size: " + pageSize);
        return prescriptionRepository.findAll(pageNumber, pageSize);
    }
    
    /**
     * Count total prescriptions
     */
    public Long count() {
        return prescriptionRepository.count();
    }
    
    // Private helper methods
    
    private void validatePrescriptionData(String medicationName, String dosage, 
                                         String frequency, Integer durationDays) {
        if (medicationName == null || medicationName.trim().isEmpty()) {
            throw new ValidationException("Medication name is required");
        }
        
        if (dosage == null || dosage.trim().isEmpty()) {
            throw new ValidationException("Dosage is required");
        }
        
        if (frequency == null || frequency.trim().isEmpty()) {
            throw new ValidationException("Frequency is required");
        }
        
        if (durationDays == null || durationDays < 1) {
            throw new ValidationException("Duration must be at least 1 day");
        }
        
        if (durationDays > 365) {
            throw new ValidationException("Duration cannot exceed 365 days");
        }
    }
}