package com.dh.roomly.common.validation;

import com.dh.roomly.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IsValidEmailValidator implements ConstraintValidator<IsValidEmail, String> {

    private final IUserRepository IUserRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !IUserRepository.existsByEmail(s);
    }
}
