package com.healthcare.config;

/**
 * Security configuration constants for authentication and authorization.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public final class SecurityConfig {
    
    private SecurityConfig() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // ===================================
    // Realm Configuration
    // ===================================
    public static final String REALM_NAME = "healthcare-realm";
    public static final String JDBC_REALM_NAME = "jdbcRealm";
    public static final String JAAS_CONTEXT = "healthcare";
    
    // ===================================
    // Role Names
    // ===================================
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String ROLE_PATIENT = "PATIENT";
    
    // ===================================
    // Security Constraints
    // ===================================
    public static final String[] PUBLIC_URLS = {
        "/",
        "/index.jsp",
        "/landing.jsp",
        "/assets/*",
        "/api/auth/login",
        "/api/auth/register"
    };
    
    public static final String[] ADMIN_URLS = {
        "/admin/*",
        "/api/admin/*",
        "/WEB-INF/jsp/admin/*"
    };
    
    public static final String[] DOCTOR_URLS = {
        "/doctor/*",
        "/api/doctors/*",
        "/api/prescriptions/*",
        "/api/medical-records/*"
    };
    
    public static final String[] PATIENT_URLS = {
        "/patient/*",
        "/api/patients/*",
        "/api/appointments/*"
    };
    
    // ===================================
    // Password Policy
    // ===================================
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final boolean REQUIRE_UPPERCASE = true;
    public static final boolean REQUIRE_LOWERCASE = true;
    public static final boolean REQUIRE_DIGIT = true;
    public static final boolean REQUIRE_SPECIAL_CHAR = true;
    public static final String SPECIAL_CHARS = "@#$%^&+=!";
    
    // ===================================
    // Account Lockout Policy
    // ===================================
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int LOCKOUT_DURATION_MINUTES = 30;
    public static final boolean ENABLE_ACCOUNT_LOCKOUT = true;
    
    // ===================================
    // Session Security
    // ===================================
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final boolean ENABLE_HTTPS_ONLY = false; // Set to true in production
    public static final boolean ENABLE_SECURE_COOKIE = false; // Set to true in production
    public static final boolean ENABLE_HTTP_ONLY_COOKIE = true;
    
    // ===================================
    // JWT Configuration
    // ===================================
    public static final String JWT_SECRET_KEY = "healthcare-system-secret-key-change-in-production";
    public static final String JWT_ISSUER = "healthcare-system";
    public static final String JWT_AUDIENCE = "healthcare-users";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final long JWT_REFRESH_EXPIRATION_MS = 604800000; // 7 days
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    // ===================================
    // CORS Configuration
    // ===================================
    public static final String[] ALLOWED_ORIGINS = {
        "http://localhost:8080",
        "http://localhost:3000",
        "http://localhost:4200"
    };
    
    public static final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    };
    
    public static final String[] ALLOWED_HEADERS = {
        "Origin",
        "Content-Type",
        "Accept",
        "Authorization",
        "X-Requested-With"
    };
    
    public static final boolean ALLOW_CREDENTIALS = true;
    public static final int CORS_MAX_AGE = 3600;
    
    // ===================================
    // Encryption Configuration
    // ===================================
    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final int ENCRYPTION_KEY_SIZE = 256;
    public static final String HASH_ALGORITHM = "SHA-256";
    
    // ===================================
    // Security Headers
    // ===================================
    public static final String HEADER_X_FRAME_OPTIONS = "DENY";
    public static final String HEADER_X_CONTENT_TYPE_OPTIONS = "nosniff";
    public static final String HEADER_X_XSS_PROTECTION = "1; mode=block";
    public static final String HEADER_CONTENT_SECURITY_POLICY = 
        "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline';";
}