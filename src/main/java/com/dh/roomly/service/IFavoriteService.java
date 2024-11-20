package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTOOutput;

import java.util.List;

public interface IFavoriteService {
    void addFavoriteProperty(Long userId, Long propertyId);
    void removeFavoriteProperty(Long userId, Long propertyId);
    List<PropertyDTOOutput> getFavoriteProperties(Long userId);

    void addFavoritePropertyUserLogged(Long propertyId);
    void removeFavoritePropertyUserLogged(Long propertyId);
    List<PropertyDTOOutput> getFavoritePropertiesUserLogged();
}