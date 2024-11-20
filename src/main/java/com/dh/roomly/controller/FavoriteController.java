package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.exception.UserNotAuthenticatedException;
import com.dh.roomly.repository.IUserRepository;
import com.dh.roomly.service.IFavoriteService;
import com.dh.roomly.service.impl.JwtService;
import com.dh.roomly.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/favorites")
@AllArgsConstructor
public class FavoriteController {
    private final IFavoriteService favoriteService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/{userId}/add/{propertyId}")
    @Operation(summary = "Agregar la propiedad a los favoritos del usuario")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long userId,
            @PathVariable Long propertyId
    ) {
        favoriteService.addFavoriteProperty(userId, propertyId);
        return ResponseEntity.ok("Propiedad agregada a favoritos.");
    }

    @DeleteMapping("/{userId}/remove/{propertyId}")
    @Operation(summary = "Remover la propiedad de los favoritos del usuario")
    public ResponseEntity<String> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long propertyId
    ) {
        favoriteService.removeFavoriteProperty(userId, propertyId);
        return ResponseEntity.ok("Propiedad removida de favoritos.");
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "Listar las propiedades favoritas del usuario")
    public ResponseEntity<List<PropertyDTOOutput>> getFavorites(@PathVariable Long userId) {
        List<PropertyDTOOutput> favorites = favoriteService.getFavoriteProperties(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/add/{propertyId}")
    @Operation(summary = "Agregar la propiedad a los favoritos del usuario autenticado")
    public ResponseEntity<String> addFavoriteUserLogged(
            @PathVariable Long propertyId,
            @RequestHeader("Authorization") String token
    ) {
        UserEntity user = userServiceImpl.getUserFromToken(token);
        favoriteService.addFavoriteProperty(user.getId(),propertyId);
        return ResponseEntity.ok("Propiedad agregada a favoritos.");
    }

    @DeleteMapping("/remove/{propertyId}")
    @Operation(summary = "Remover la propiedad de los favoritos del usuario autenticado")
    public ResponseEntity<String> removeFavoriteUserLogged(
            @PathVariable Long propertyId,
            @RequestHeader("Authorization") String token
    ) {
        UserEntity user = userServiceImpl.getUserFromToken(token);
        favoriteService.removeFavoriteProperty(user.getId(),propertyId);
        return ResponseEntity.ok("Propiedad removida de favoritos.");
    }

    @GetMapping("/all")
    @Operation(summary = "Listar las propiedades favoritas del usuario autenticado")
    public ResponseEntity<?> getFavoritesUserLogged(@RequestHeader("Authorization") String token) {
        try {
            UserEntity user = userServiceImpl.getUserFromToken(token);
            return ResponseEntity.ok(favoriteService.getFavoriteProperties(user.getId()));
        } catch (UserNotAuthenticatedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
