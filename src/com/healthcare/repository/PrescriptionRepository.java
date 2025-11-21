package com.healthcare.repository;

import com.healthcare.entity.Prescription;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Prescription entity data access operations.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class PrescriptionRepository {
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find prescription by ID
     */
    public Optional<Prescription> findById(Long id) {
        try {
            Prescription prescription = entityManager.find(Prescription.class, id);
            return Optional.ofNullable(prescription);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find all prescriptions
     */
    public List<Prescription> findAll() {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findAll", Prescription.class);
        return query.getResultList();
    }
    
    /**
     * Find prescriptions by patient
     */
    public List<Prescription> findByPatient(Long patientId) {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findByPatient", Prescription.class);
        query.setParameter("patientId", patientId);
        return query.getResultList();
    }
    
    /**
     * Find prescriptions by doctor
     */
    public List<Prescription> findByDoctor(Long doctorId) {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findByDoctor", Prescription.class);
        query.setParameter("doctorId", doctorId);
        return query.getResultList();
    }
    
    /**
     * Find active prescriptions for patient
     */
    public List<Prescription> findActive(Long patientId) {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findActive", Prescription.class);
        query.setParameter("patientId", patientId);
        return query.getResultList();
    }
    
    /**
     * Find prescriptions by appointment
     */
    public List<Prescription> findByAppointment(Long appointmentId) {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findByAppointment", Prescription.class);
        query.setParameter("appointmentId", appointmentId);
        return query.getResultList();
    }
    
    /**
     * Save new prescription
     */
    public Prescription save(Prescription prescription) {
        entityManager.persist(prescription);
        entityManager.flush();
        return prescription;
    }
    
    /**
     * Update existing prescription
     */
    public Prescription update(Prescription prescription) {
        Prescription merged = entityManager.merge(prescription);
        entityManager.flush();
        return merged;
    }
    
    /**
     * Delete prescription
     */
    public void delete(Long id) {
        Prescription prescription = entityManager.find(Prescription.class, id);
        if (prescription != null) {
            entityManager.remove(prescription);
            entityManager.flush();
        }
    }
    
    /**
     * Find prescriptions with pagination
     */
    public List<Prescription> findAll(int pageNumber, int pageSize) {
        TypedQuery<Prescription> query = entityManager.createNamedQuery("Prescription.findAll", Prescription.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    
    /**
     * Count total prescriptions
     */
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(p) FROM Prescription p", Long.class);
        return query.getSingleResult();
    }
}