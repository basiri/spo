package com.abs.spo.controller.valdator;

import com.abs.spo.controller.dto.WorkforceRequestDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom validity implementation for requestDTO
 */
public class RoomValidityValidator implements ConstraintValidator<RoomValidity, WorkforceRequestDTO> {
    @Override
    public void initialize(RoomValidity constraintAnnotation) {

    }

    @Override
    public boolean isValid(WorkforceRequestDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        int sumRooms=0;
        for (int i=0;i<dto.getRooms().length;i++) {
            if (dto.getRooms()[i] < 1) {
                return false;
            }
            sumRooms+=dto.getRooms()[i];
        }
        if (sumRooms>100){
            return false;
        }
        return true;

    }
}
