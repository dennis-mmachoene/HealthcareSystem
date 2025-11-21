package com.healthcare.entity;

/**
 * Enumeration representing user roles in the healthcare system.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public enum UserRole {
    
    /**
     * System administrator with full access
     */
    ADMIN("Administrator", "Full system access and management"),
    
    /**
     * Medical doctor with clinical privileges
     */
    DOCTOR("Doctor", "Medical professional with patient care access"),
    
    /**
     * Patient using the healthcare services
     */
    PATIENT("Patient", "Healthcare service recipient");
    
    private final String displayName;
    private final String description;
    
    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get UserRole from string value (case-insensitive)
     */
    public static UserRole fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        
        throw new IllegalArgumentException("Unknown user role: " + value);
    }
}