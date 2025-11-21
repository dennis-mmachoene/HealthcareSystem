package com.healthcare.repository;

import com.healthcare.entity.ApprovalStatus;
import com.healthcare.entity.Doctor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Doctor entity data access operations.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class DoctorRepository {
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find doctor by ID
     */
    public Optional<Doctor> findById(Long id) {
        try {
            Doctor doctor = entityManager.find(Doctor.class, id);
            return Optional.ofNullable(doctor);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find doctor by user ID
     */
    public Optional<Doctor> findByUserId(Long userId) {
        try {
            TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findByUserId", Doctor.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find doctor by license number
     */
    public Optional<Doctor> findByLicenseNumber(String licenseNumber) {
        try {
            TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findByLicenseNumber", Doctor.class);
            query.setParameter("licenseNumber", licenseNumber);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find all doctors
     */
    public List<Doctor> findAll() {
        TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findAll", Doctor.class);
        return query.getResultList();
    }
    
    /**
     * Find doctors by specialization
     */
    public List<Doctor> findBySpecialization(String specialization) {
        TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findBySpecialization", Doctor.class);
        query.setParameter("specialization", specialization);
        return query.getResultList();
    }
    
    /**
     * Find doctors by approval status
     */
    public List<Doctor> findByApprovalStatus(ApprovalStatus status) {
        TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findByApprovalStatus", Doctor.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    /**
     * Find available doctors (approved and available)
     */
    public List<Doctor> findAvailable() {
        TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findAvailable", Doctor.class);
        return query.getResultList();
    }
    
    /**
     * Count doctors by approval status
     */
    public Long countByStatus(ApprovalStatus status) {
        TypedQuery<Long> query = entityManager.createNamedQuery("Doctor.countByStatus", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
    
    /**
     * Save new doctor
     */
    public Doctor save(Doctor doctor) {
        entityManager.persist(doctor);
        entityManager.flush();
        return doctor;
    }
    
    /**
     * Update existing doctor
     */
    public Doctor update(Doctor doctor) {
        Doctor merged = entityManager.merge(doctor);
        entityManager.flush();
        return merged;
    }
    
    /**
     * Delete doctor
     */
    public void delete(Long id) {
        Doctor doctor = entityManager.find(Doctor.class, id);
        if (doctor != null) {
            entityManager.remove(doctor);
            entityManager.flush();
        }
    }
    
    /**
     * Get distinct specializations
     */
    public List<String> getDistinctSpecializations() {
        TypedQuery<String> query = entityManager.createQuery(
            "SELECT DISTINCT d.specialization FROM Doctor d WHERE d.approvalStatus = 'APPROVED' ORDER BY d.specialization", 
            String.class);
        return query.getResultList();
    }
    
    /**
     * Find doctors with pagination
     */
    public List<Doctor> findAll(int pageNumber, int pageSize) {
        TypedQuery<Doctor> query = entityManager.createNamedQuery("Doctor.findAll", Doctor.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    
    /**
     * Count total doctors
     */
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(d) FROM Doctor d", Long.class);
        return query.getSingleResult();
    }
}