package com.healthcare.repository;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.AppointmentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Appointment entity data access operations.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class AppointmentRepository {
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find appointment by ID
     */
    public Optional<Appointment> findById(Long id) {
        try {
            Appointment appointment = entityManager.find(Appointment.class, id);
            return Optional.ofNullable(appointment);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find all appointments
     */
    public List<Appointment> findAll() {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findAll", Appointment.class);
        return query.getResultList();
    }
    
    /**
     * Find appointments by patient
     */
    public List<Appointment> findByPatient(Long patientId) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByPatient", Appointment.class);
        query.setParameter("patientId", patientId);
        return query.getResultList();
    }
    
    /**
     * Find appointments by doctor
     */
    public List<Appointment> findByDoctor(Long doctorId) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByDoctor", Appointment.class);
        query.setParameter("doctorId", doctorId);
        return query.getResultList();
    }
    
    /**
     * Find appointments by status
     */
    public List<Appointment> findByStatus(AppointmentStatus status) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByStatus", Appointment.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    /**
     * Find appointments by date
     */
    public List<Appointment> findByDate(LocalDate date) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByDate", Appointment.class);
        query.setParameter("date", date);
        return query.getResultList();
    }
    
    /**
     * Find upcoming appointments
     */
    public List<Appointment> findUpcoming() {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findUpcoming", Appointment.class);
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }
    
    /**
     * Find doctor's schedule for a specific date
     */
    public List<Appointment> findDoctorSchedule(Long doctorId, LocalDate date) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findDoctorSchedule", Appointment.class);
        query.setParameter("doctorId", doctorId);
        query.setParameter("date", date);
        return query.getResultList();
    }
    
    /**
     * Save new appointment
     */
    public Appointment save(Appointment appointment) {
        entityManager.persist(appointment);
        entityManager.flush();
        return appointment;
    }
    
    /**
     * Update existing appointment
     */
    public Appointment update(Appointment appointment) {
        Appointment merged = entityManager.merge(appointment);
        entityManager.flush();
        return merged;
    }
    
    /**
     * Delete appointment
     */
    public void delete(Long id) {
        Appointment appointment = entityManager.find(Appointment.class, id);
        if (appointment != null) {
            entityManager.remove(appointment);
            entityManager.flush();
        }
    }
    
    /**
     * Count appointments by status
     */
    public Long countByStatus(AppointmentStatus status) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Appointment a WHERE a.status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
    
    /**
     * Find appointments with pagination
     */
    public List<Appointment> findAll(int pageNumber, int pageSize) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findAll", Appointment.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    
    /**
     * Count total appointments
     */
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Appointment a", Long.class);
        return query.getSingleResult();
    }
}