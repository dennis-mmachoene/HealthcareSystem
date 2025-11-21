// src/com/healthcare/mapper/PatientMapper.java
package com.healthcare.mapper;

import com.healthcare.dto.PatientDTO;
import com.healthcare.entity.Patient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Mapper for converting between Patient entity and PatientDTO.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@ApplicationScoped
public class PatientMapper {
    
    @Inject
    private UserMapper userMapper;
    
    /**
     * Convert Patient entity to PatientDTO
     */
    public PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setUser(userMapper.toDTO(patient.getUser()));
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setCity(patient.getCity());
        dto.setState(patient.getState());
        dto.setPostalCode(patient.getPostalCode());
        dto.setBloodGroup(patient.getBloodGroup() != null ? patient.getBloodGroup().name() : null);
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setAllergies(patient.getAllergies());
        dto.setCurrentMedications(patient.getCurrentMedications());
        dto.setAge(patient.getAge());
        
        return dto;
    }
    
    /**
     * Convert PatientDTO to Patient entity
     */
    public Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setPostalCode(dto.getPostalCode());
        patient.setEmergencyContactName(dto.getEmergencyContactName());
        patient.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAllergies(dto.getAllergies());
        patient.setCurrentMedications(dto.getCurrentMedications());
        
        return patient;
    }
    
    /**
     * Update existing Patient entity with data from PatientDTO
     */
    public void updateEntity(Patient patient, PatientDTO dto) {
        if (patient == null || dto == null) {
            return;
        }
        
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setPostalCode(dto.getPostalCode());
        patient.setEmergencyContactName(dto.getEmergencyContactName());
        patient.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAllergies(dto.getAllergies());
        patient.setCurrentMedications(dto.getCurrentMedications());
    }
}