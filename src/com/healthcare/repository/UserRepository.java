package com.healthcare.repository;

import com.healthcare.entity.User;
import com.healthcare.entity.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity data access operations.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class UserRepository {
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        try {
            User user = entityManager.find(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createNamedQuery("User.findByEmail", User.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find all users
     */
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }
    
    /**
     * Find users by role
     */
    public List<User> findByRole(UserRole role) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByRole", User.class);
        query.setParameter("role", role);
        return query.getResultList();
    }
    
    /**
     * Find active users
     */
    public List<User> findActiveUsers() {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findActiveUsers", User.class);
        return query.getResultList();
    }
    
    /**
     * Count users by role
     */
    public Long countByRole(UserRole role) {
        TypedQuery<Long> query = entityManager.createNamedQuery("User.countByRole", Long.class);
        query.setParameter("role", role);
        return query.getSingleResult();
    }
    
    /**
     * Save new user
     */
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
    
    /**
     * Update existing user
     */
    public User update(User user) {
        User merged = entityManager.merge(user);
        entityManager.flush();
        return merged;
    }
    
    /**
     * Delete user
     */
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            entityManager.flush();
        }
    }
    
    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Search users by name
     */
    public List<User> searchByName(String searchTerm) {
        TypedQuery<User> query = entityManager.createQuery(
            "SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(:search) " +
            "OR LOWER(u.lastName) LIKE LOWER(:search) ORDER BY u.lastName, u.firstName", 
            User.class);
        query.setParameter("search", "%" + searchTerm + "%");
        return query.getResultList();
    }
    
    /**
     * Find users with pagination
     */
    public List<User> findAll(int pageNumber, int pageSize) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
    
    /**
     * Count total users
     */
    public Long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }
}