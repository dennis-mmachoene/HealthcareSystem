package com.healthcare.service;

import com.healthcare.entity.User;
import com.healthcare.entity.UserRole;
import com.healthcare.exception.NotFoundException;
import com.healthcare.exception.ValidationException;
import com.healthcare.repository.UserRepository;
import com.healthcare.util.PasswordUtils;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for User-related business operations.
 * Provides user management, authentication, and authorization logic.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserService {
    
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    
    @Inject
    private UserRepository userRepository;
    
    /**
     * Find user by ID
     */
    public User findById(Long id) {
        LOGGER.info("Finding user by ID: " + id);
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }
    
    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        LOGGER.info("Finding user by email: " + email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }
    
    /**
     * Find all users
     */
    public List<User> findAll() {
        LOGGER.info("Finding all users");
        return userRepository.findAll();
    }
    
    /**
     * Find users by role
     */
    public List<User> findByRole(UserRole role) {
        LOGGER.info("Finding users by role: " + role);
        return userRepository.findByRole(role);
    }
    
    /**
     * Create new user
     */
    public User createUser(User user, String plainPassword) {
        LOGGER.info("Creating new user: " + user.getEmail());
        
        // Validate user
        validateUser(user, plainPassword);
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already exists: " + user.getEmail());
        }
        
        // Hash password
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);
        
        // Save user
        User savedUser = userRepository.save(user);
        LOGGER.info("User created successfully with ID: " + savedUser.getId());
        
        return savedUser;
    }
    
    /**
     * Update existing user
     */
    public User updateUser(User user) {
        LOGGER.info("Updating user with ID: " + user.getId());
        
        // Verify user exists
        User existingUser = findById(user.getId());
        
        // Check email uniqueness if changed
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new ValidationException("Email already exists: " + user.getEmail());
            }
        }
        
        // Update fields
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhone(user.getPhone());
        existingUser.setIsActive(user.getIsActive());
        existingUser.setIsVerified(user.getIsVerified());
        
        User updatedUser = userRepository.update(existingUser);
        LOGGER.info("User updated successfully");
        
        return updatedUser;
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with ID: " + id);
        
        // Verify user exists
        findById(id);
        
        userRepository.delete(id);
        LOGGER.info("User deleted successfully");
    }
    
    /**
     * Authenticate user
     */
    public User authenticate(String email, String plainPassword) {
        LOGGER.info("Authenticating user: " + email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("Invalid email or password"));
        
        // Verify password
        if (!PasswordUtils.verifyPassword(plainPassword, user.getPasswordHash())) {
            throw new ValidationException("Invalid email or password");
        }
        
        // Check if user is active
        if (!user.getIsActive()) {
            throw new ValidationException("User account is deactivated");
        }
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.update(user);
        
        LOGGER.info("User authenticated successfully");
        return user;
    }
    
    /**
     * Change user password
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        LOGGER.info("Changing password for user ID: " + userId);
        
        User user = findById(userId);
        
        // Verify old password
        if (!PasswordUtils.verifyPassword(oldPassword, user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 8) {
            throw new ValidationException("New password must be at least 8 characters");
        }
        
        // Hash and update password
        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        user.setPasswordHash(hashedPassword);
        userRepository.update(user);
        
        LOGGER.info("Password changed successfully");
    }
    
    /**
     * Activate user account
     */
    public void activateUser(Long userId) {
        LOGGER.info("Activating user with ID: " + userId);
        
        User user = findById(userId);
        user.setIsActive(true);
        user.setIsVerified(true);
        userRepository.update(user);
        
        LOGGER.info("User activated successfully");
    }
    
    /**
     * Deactivate user account
     */
    public void deactivateUser(Long userId) {
        LOGGER.info("Deactivating user with ID: " + userId);
        
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.update(user);
        
        LOGGER.info("User deactivated successfully");
    }
    
    /**
     * Count users by role
     */
    public Long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
    
    /**
     * Search users by name
     */
    public List<User> searchByName(String searchTerm) {
        LOGGER.info("Searching users by name: " + searchTerm);
        return userRepository.searchByName(searchTerm);
    }
    
    /**
     * Find users with pagination
     */
    public List<User> findAll(int pageNumber, int pageSize) {
        LOGGER.info("Finding users - Page: " + pageNumber + ", Size: " + pageSize);
        return userRepository.findAll(pageNumber, pageSize);
    }
    
    /**
     * Count total users
     */
    public Long count() {
        return userRepository.count();
    }
    
    // Private helper methods
    
    private void validateUser(User user, String plainPassword) {
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
        
        if (user.getRole() == null) {
            throw new ValidationException("Role is required");
        }
        
        if (plainPassword == null || plainPassword.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
    }
}