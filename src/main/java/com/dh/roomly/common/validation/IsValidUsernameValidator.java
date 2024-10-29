package com.dh.roomly.common.validation;

import com.dh.roomly.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IsValidUsernameValidator implements ConstraintValidator<IsValidUsername, String> {

    private final UserRepository userRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByUsername(s);
    }
}
