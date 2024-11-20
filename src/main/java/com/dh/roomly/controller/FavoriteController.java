package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.service.IFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/favorites")
@AllArgsConstructor
public class FavoriteController {
    private final IFavoriteService favoriteService;

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
            @PathVariable Long propertyId
    ) {
        favoriteService.addFavoritePropertyUserLogged(propertyId);
        return ResponseEntity.ok("Propiedad agregada a favoritos.");
    }

    @DeleteMapping("/remove/{propertyId}")
    @Operation(summary = "Remover la propiedad de los favoritos del usuario autenticado")
    public ResponseEntity<String> removeFavoriteUserLogged(
            @PathVariable Long propertyId
    ) {
        favoriteService.removeFavoritePropertyUserLogged(propertyId);
        return ResponseEntity.ok("Propiedad removida de favoritos.");
    }

    @GetMapping("/all")
    @Operation(summary = "Listar las propiedades favoritas del usuario autenticado")
    public ResponseEntity<List<PropertyDTOOutput>> getFavoritesUserLogged() {

        List<PropertyDTOOutput> favorites = favoriteService.getFavoritePropertiesUserLogged();
        return ResponseEntity.ok(favorites);
    }

}
