package com.healthcare.config;

/**
 * Application-wide constants for the Healthcare Management System.
 * Contains configuration values, default settings, and magic numbers.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public final class AppConstants {
    
    // Prevent instantiation
    private AppConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // ===================================
    // Application Information
    // ===================================
    public static final String APP_NAME = "Smart Healthcare Management System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_CONTEXT_ROOT = "healthcare";
    
    // ===================================
    // Date and Time Formats
    // ===================================
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_DATETIME_FORMAT = "dd MMM yyyy HH:mm";
    
    // ===================================
    // Pagination Defaults
    // ===================================
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    
    // ===================================
    // Appointment Settings
    // ===================================
    public static final int DEFAULT_APPOINTMENT_DURATION = 30; // minutes
    public static final int MIN_APPOINTMENT_DURATION = 15; // minutes
    public static final int MAX_APPOINTMENT_DURATION = 120; // minutes
    public static final int APPOINTMENT_REMINDER_HOURS = 24; // hours before
    
    // ===================================
    // Business Hours
    // ===================================
    public static final String BUSINESS_START_TIME = "08:00:00";
    public static final String BUSINESS_END_TIME = "18:00:00";
    public static final int WORKING_DAYS_START = 1; // Monday
    public static final int WORKING_DAYS_END = 5; // Friday
    
    // ===================================
    // Session Settings
    // ===================================
    public static final String SESSION_USER_KEY = "currentUser";
    public static final String SESSION_USER_ID_KEY = "userId";
    public static final String SESSION_USER_ROLE_KEY = "userRole";
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    
    // ===================================
    // Security Settings
    // ===================================
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 50;
    public static final int BCRYPT_STRENGTH = 10;
    public static final String JWT_ISSUER = "healthcare-system";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    
    // ===================================
    // Validation Patterns
    // ===================================
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String LICENSE_NUMBER_PATTERN = "^[A-Z]{2}-[A-Z]{3}-[0-9]{3}-[0-9]{4}$";
    
    // ===================================
    // File Upload Settings
    // ===================================
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String[] ALLOWED_FILE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".pdf", ".doc", ".docx"};
    public static final String UPLOAD_DIRECTORY = "/uploads";
    
    // ===================================
    // Database Settings
    // ===================================
    public static final String DB_JNDI_NAME = "jdbc/HealthcareDB";
    public static final int DB_CONNECTION_TIMEOUT = 30; // seconds
    public static final int DB_MAX_POOL_SIZE = 20;
    
    // ===================================
    // Email Settings
    // ===================================
    public static final String FROM_EMAIL = "noreply@healthcare.com";
    public static final String SUPPORT_EMAIL = "support@healthcare.com";
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final int SMTP_PORT = 587;
    
    // ===================================
    // Notification Settings
    // ===================================
    public static final int MAX_UNREAD_NOTIFICATIONS = 50;
    public static final int NOTIFICATION_RETENTION_DAYS = 30;
    
    // ===================================
    // API Settings
    // ===================================
    public static final String API_BASE_PATH = "/api";
    public static final String API_VERSION = "v1";
    public static final int API_RATE_LIMIT = 100; // requests per minute
    
    // ===================================
    // Cache Settings
    // ===================================
    public static final int CACHE_EXPIRATION_SECONDS = 300; // 5 minutes
    public static final int MAX_CACHE_SIZE = 1000;
    
    // ===================================
    // Error Messages
    // ===================================
    public static final String ERROR_REQUIRED_FIELD = "This field is required";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number format";
    public static final String ERROR_PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
    public static final String ERROR_UNAUTHORIZED = "You are not authorized to perform this action";
    public static final String ERROR_NOT_FOUND = "The requested resource was not found";
    public static final String ERROR_INTERNAL_SERVER = "An internal server error occurred";
    
    // ===================================
    // Success Messages
    // ===================================
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logout successful";
    public static final String SUCCESS_REGISTRATION = "Registration successful";
    public static final String SUCCESS_UPDATE = "Update successful";
    public static final String SUCCESS_DELETE = "Delete successful";
    public static final String SUCCESS_APPOINTMENT_CREATED = "Appointment created successfully";
    public static final String SUCCESS_APPOINTMENT_CANCELLED = "Appointment cancelled successfully";
    
    // ===================================
    // System Settings
    // ===================================
    public static final boolean ENABLE_REGISTRATION = true;
    public static final boolean REQUIRE_EMAIL_VERIFICATION = true;
    public static final boolean ENABLE_AUDIT_LOG = true;
    public static final String DEFAULT_TIMEZONE = "Africa/Johannesburg";
    public static final String DEFAULT_LOCALE = "en_ZA";
    
    // ===================================
    // JMS Queue Names
    // ===================================
    public static final String JMS_NOTIFICATION_QUEUE = "jms/NotificationQueue";
    public static final String JMS_SYSTEM_NOTIFICATION_QUEUE = "jms/SystemNotificationQueue";
    public static final String JMS_CONNECTION_FACTORY = "jms/ConnectionFactory";
    
    // ===================================
    // Scheduled Job Settings
    // ===================================
    public static final String CRON_CLEANUP_JOB = "0 0 2 * * ?"; // Daily at 2 AM
    public static final String CRON_REMINDER_JOB = "0 0 8 * * ?"; // Daily at 8 AM
    public static final String CRON_ANALYTICS_JOB = "0 0 * * * ?"; // Every hour
}