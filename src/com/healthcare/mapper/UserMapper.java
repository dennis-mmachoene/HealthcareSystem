package com.healthcare.mapper;

import com.healthcare.dto.UserDTO;
import com.healthcare.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for converting between User entity and UserDTO.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class UserMapper {
    
    /**
     * Convert User entity to UserDTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setIsVerified(user.getIsVerified());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        
        return dto;
    }
    
    /**
     * Convert UserDTO to User entity (for updates, not creation)
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setIsActive(dto.getIsActive());
        user.setIsVerified(dto.getIsVerified());
        
        return user;
    }
    
    /**
     * Update existing User entity with data from UserDTO
     */
    public void updateEntity(User user, UserDTO dto) {
        if (user == null || dto == null) {
            return;
        }
        
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setIsActive(dto.getIsActive());
        user.setIsVerified(dto.getIsVerified());
    }
}