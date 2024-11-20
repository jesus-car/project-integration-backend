package com.dh.roomly.dto.impl;


import com.dh.roomly.dto.IDTOEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDetailsDTOOutput implements IDTOEntity {
    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerNight;
    private CityWithCountrySimpleDTO city;
    private String exactAddress;
    private Short maxCapacity;
    private Short numRooms;
    private Short numBeds;
    private Short numBathrooms;
    private UserSimpleDTOOutput owner;
    private CategoryDTOOutput category;
    private List<FeatureDTOOutput> features;
    private String mainPhotoUrl;
    private List<String> photoUrls;
    private List<BookingDTOOutput> bookings;
}