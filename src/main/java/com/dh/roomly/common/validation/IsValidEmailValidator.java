package com.dh.roomly.common.validation;

import com.dh.roomly.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IsValidEmailValidator implements ConstraintValidator<IsValidEmail, String> {

    private final UserRepository userRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(s);
    }
}
