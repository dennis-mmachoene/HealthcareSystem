package com.healthcare.service;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.AppointmentStatus;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.entity.User;
import com.healthcare.exception.NotFoundException;
import com.healthcare.exception.ValidationException;
import com.healthcare.repository.AppointmentRepository;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Appointment-related business operations.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AppointmentService {
    
    private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Inject
    private PatientService patientService;
    
    @Inject
    private DoctorService doctorService;
    
    @Inject
    private NotificationService notificationService;
    
    /**
     * Find appointment by ID
     */
    public Appointment findById(Long id) {
        LOGGER.info("Finding appointment by ID: " + id);
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Appointment not found with ID: " + id));
    }
    
    /**
     * Find all appointments
     */
    public List<Appointment> findAll() {
        LOGGER.info("Finding all appointments");
        return appointmentRepository.findAll();
    }
    
    /**
     * Find appointments by patient
     */
    public List<Appointment> findByPatient(Long patientId) {
        LOGGER.info("Finding appointments for patient ID: " + patientId);
        return appointmentRepository.findByPatient(patientId);
    }
    
    /**
     * Find appointments by doctor
     */
    public List<Appointment> findByDoctor(Long doctorId) {
        LOGGER.info("Finding appointments for doctor ID: " + doctorId);
        return appointmentRepository.findByDoctor(doctorId);
    }
    
    /**
     * Find upcoming appointments
     */
    public List<Appointment> findUpcoming() {
        LOGGER.info("Finding upcoming appointments");
        return appointmentRepository.findUpcoming();
    }
    
    /**
     * Create new appointment
     */
    public Appointment createAppointment(Long patientId, Long doctorId, 
                                         LocalDate appointmentDate, LocalTime appointmentTime,
                                         Integer durationMinutes, String reason) {
        LOGGER.info("Creating new appointment");
        
        // Validate inputs
        if (appointmentDate == null || appointmentTime == null) {
            throw new ValidationException("Appointment date and time are required");
        }
        
        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Appointment date cannot be in the past");
        }
        
        // Get patient and doctor
        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.findById(doctorId);
        
        // Verify doctor is approved and available
        if (!doctor.isApproved()) {
            throw new ValidationException("Doctor is not approved");
        }
        
        if (!doctor.isAvailable()) {
            throw new ValidationException("Doctor is not available");
        }
        
        // Check for scheduling conflicts
        List<Appointment> existingAppointments = appointmentRepository
            .findDoctorSchedule(doctorId, appointmentDate);
        
        for (Appointment existing : existingAppointments) {
            if (hasTimeConflict(existing, appointmentTime, durationMinutes)) {
                throw new ValidationException("Time slot is not available");
            }
        }
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setDurationMinutes(durationMinutes != null ? durationMinutes : 30);
        appointment.setReason(reason);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        LOGGER.info("Appointment created successfully with ID: " + savedAppointment.getId());
        
        // Send notification to doctor
        notificationService.sendAppointmentNotification(savedAppointment);
        
        return savedAppointment;
    }
    
    /**
     * Update appointment
     */
    public Appointment updateAppointment(Long appointmentId, LocalDate appointmentDate, 
                                         LocalTime appointmentTime, Integer durationMinutes, 
                                         String reason, String notes) {
        LOGGER.info("Updating appointment ID: " + appointmentId);
        
        Appointment appointment = findById(appointmentId);
        
        // Only allow updates for scheduled appointments
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new ValidationException("Can only update scheduled appointments");
        }
        
        // Update fields
        if (appointmentDate != null) {
            appointment.setAppointmentDate(appointmentDate);
        }
        if (appointmentTime != null) {
            appointment.setAppointmentTime(appointmentTime);
        }
        if (durationMinutes != null) {
            appointment.setDurationMinutes(durationMinutes);
        }
        if (reason != null) {
            appointment.setReason(reason);
        }
        if (notes != null) {
            appointment.setNotes(notes);
        }
        
        Appointment updatedAppointment = appointmentRepository.update(appointment);
        LOGGER.info("Appointment updated successfully");
        
        return updatedAppointment;
    }
    
    /**
     * Cancel appointment
     */
    public void cancelAppointment(Long appointmentId, Long userId, String reason) {
        LOGGER.info("Cancelling appointment ID: " + appointmentId);
        
        Appointment appointment = findById(appointmentId);
        
        // Only allow cancellation of scheduled appointments
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new ValidationException("Can only cancel scheduled appointments");
        }
        
        // Cancel appointment
        User cancelledBy = new User();
        cancelledBy.setId(userId);
        appointment.cancel(cancelledBy, reason);
        
        appointmentRepository.update(appointment);
        
        // Send cancellation notification
        notificationService.sendAppointmentCancellationNotification(appointment);
        
        LOGGER.info("Appointment cancelled successfully");
    }
    
    /**
     * Complete appointment
     */
    public void completeAppointment(Long appointmentId) {
        LOGGER.info("Completing appointment ID: " + appointmentId);
        
        Appointment appointment = findById(appointmentId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.update(appointment);
        
        LOGGER.info("Appointment marked as completed");
    }
    
    /**
     * Mark appointment as no-show
     */
    public void markNoShow(Long appointmentId) {
        LOGGER.info("Marking appointment ID: " + appointmentId + " as no-show");
        
        Appointment appointment = findById(appointmentId);
        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointmentRepository.update(appointment);
        
        LOGGER.info("Appointment marked as no-show");
    }
    
    /**
     * Get doctor's schedule for a date
     */
    public List<Appointment> getDoctorSchedule(Long doctorId, LocalDate date) {
        LOGGER.info("Getting schedule for doctor ID: " + doctorId + " on date: " + date);
        return appointmentRepository.findDoctorSchedule(doctorId, date);
    }
    
    /**
     * Count appointments by status
     */
    public Long countByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }
    
    /**
     * Find appointments with pagination
     */
    public List<Appointment> findAll(int pageNumber, int pageSize) {
        LOGGER.info("Finding appointments - Page: " + pageNumber + ", Size: " + pageSize);
        return appointmentRepository.findAll(pageNumber, pageSize);
    }
    
    /**
     * Count total appointments
     */
    public Long count() {
        return appointmentRepository.count();
    }
    
    // Private helper methods
    
    private boolean hasTimeConflict(Appointment existing, LocalTime newTime, Integer newDuration) {
        LocalTime existingStart = existing.getAppointmentTime();
        LocalTime existingEnd = existingStart.plusMinutes(existing.getDurationMinutes());
        LocalTime newEnd = newTime.plusMinutes(newDuration);
        
        // Check if times overlap
        return (newTime.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }
}