package com.healthcare.repository;

import com.healthcare.entity.Patient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Patient entity data access operations.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class PatientRepository {
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find patient by ID
     */
    public Optional<Patient> findById(Long id) {
        try {
            Patient patient = entityManager.find(Patient.class, id);
            return Optional.ofNullable(patient);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find patient by user ID
     */
    public Optional<Patient> findByUserId(Long userId) {
        try {
            TypedQuery<Patient> query = entityManager.createNamedQuery("Patient.findByUserId", Patient.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find all patients
     */
    public List<Patient> findAll() {
        TypedQuery<Patient> query = entityManager.createNamedQuery("Patient.findAll", Patient.class);
        return query.getResultList();
    }
    
    /**
     * Find patients by city
     */
    public List<Patient> findByCity(String city) {
        TypedQuery<Patient> query = entityManager.createNamedQuery("Patient.findByCity", Patient.class);
        query.setParameter("city", city);
        return query.getResultList();
    }
    
    /**
     * Search patients by name
     */
    public List<Patient> searchByName(String searchTerm) {
        TypedQuery<Patient> query = entityManager.createNamedQuery("Patient.searchByName", Patient.class);
        query.setParameter("name", "%" + searchTerm + "%");
        return query.getResultList();
    }
    
    /**
     * Save new patient
     */
    public Patient save(Patient patient) {
        entityManager.persist(patient);
        entityManager.flush();
        return patient;
    }
    
    /**
     * Update existing patient
     */
    public Patient update(Patient patient) {
        Patient merged = entityManager.merge(patient);
        entityManager.flush();
        return merged;
    }
    
    /**
     * Delete patient
     */
    public void delete(Long id) {
        Patient patient = entityManager.find(Patient.class, id);
        if (patient != null) {
            entityManager.remove(patient);
            entityManager.flush();
        }
    }
    
    /**
     * Find patients with pagination
     */
    public List<Patient> findAll(int pageNumber, int pageSize) {
        TypedQuery<Patient> query = entityManager.createNamedQuery("Patient.findAll", Patient.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    
    /**
     * Count total patients
     */
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(p) FROM Patient p", Long.class);
        return query.getSingleResult();
    }
}