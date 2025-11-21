package com.healthcare.entity;

/**
 * Enumeration representing gender options.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public enum Gender {
    
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");
    
    private final String displayName;
    
    Gender(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        
        throw new IllegalArgumentException("Unknown gender: " + value);
    }
}