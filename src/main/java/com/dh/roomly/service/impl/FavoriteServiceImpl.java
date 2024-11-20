package com.dh.roomly.service.impl;

import com.dh.roomly.common.NotFound;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.entity.PropertyEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.exception.DuplicateResourceException;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.exception.UserNotAuthenticatedException;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.repository.IUserRepository;
import com.dh.roomly.service.IFavoriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements IFavoriteService {

    private final IUserRepository userRepository;
    private final IPropertyRepository propertyRepository;


    @Override
    @Transactional
    public void addFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_USER + " con ID: " + userId));

        PropertyEntity property = propertyRepository.findById(String.valueOf(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_PRODUCT + " con ID: " + propertyId));

        if (user.getFavoriteProperties().contains(property)) {
            throw new DuplicateResourceException("Propiedad ya esta en favoritos.");
        }

        user.getFavoriteProperties().add(property);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_USER + " con ID: " + userId));

        PropertyEntity property = propertyRepository.findById(String.valueOf(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_PRODUCT + " con ID: " + propertyId));

        user.getFavoriteProperties().remove(property);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<PropertyDTOOutput> getFavoriteProperties(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_USER + " con ID: " + userId));

        return user.getFavoriteProperties().stream()
                .map(property -> (PropertyDTOOutput) MappingDTO.convertToDto(property, new PropertyDTOOutput()))
                .collect(Collectors.toList());
    }

    @Override
    public void addFavoritePropertyUserLogged(Long propertyId) {
        UserEntity user = getAuthenticatedUser();
        if (user != null) {
            PropertyEntity property = propertyRepository.findById(String.valueOf(propertyId))
                    .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_PRODUCT + " con ID: " + propertyId));
            user.getFavoriteProperties().add(property);
            userRepository.save(user);
        } else {
            throw new UserNotAuthenticatedException("Usuario no Autenticado");
        }
    }

    @Override
    public void removeFavoritePropertyUserLogged(Long propertyId) {
        UserEntity user = getAuthenticatedUser();
        if (user != null) {
            PropertyEntity property = propertyRepository.findById(String.valueOf(propertyId))
                    .orElseThrow(() -> new ResourceNotFoundException(NotFound.NOT_FOUND_PRODUCT + " con ID: " + propertyId));
            // Eliminar propiedad de favoritos
            user.getFavoriteProperties().remove(property);
            userRepository.save(user);
        } else {
            throw new UserNotAuthenticatedException("Usuario no Autenticado");
        }
    }

    @Override
    public List<PropertyDTOOutput> getFavoritePropertiesUserLogged() {
        UserEntity user = getAuthenticatedUser();
        if (user == null){
            throw new UserNotAuthenticatedException("Usuario no Autenticado");
        }
        return user.getFavoriteProperties().stream()
                .map(property -> (PropertyDTOOutput) MappingDTO.convertToDto(property, new PropertyDTOOutput()))
                .collect(Collectors.toList());
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null  && authentication.isAuthenticated()) {
            System.out.println("Usuario autenticado: " + authentication.getName());
            String username = authentication.getName();
            return userRepository.findByUsername(username);
        }
        System.out.println("No hay usuario autenticado.");
        return null;
    }
}
