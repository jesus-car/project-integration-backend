package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTOOutput implements IDTOEntity {
    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerNight;
    private String exactAddress;
    private Short maxCapacity;
    private Short numRooms;
    private Short numBeds;
    private Short numBathrooms;
    private Short cityId;
    private Short countryId;
    private Long ownerId;
    private Short categoryId;
    private List<Short> featureIds;
    private String mainPhotoUrl;
    private List<String> photoUrls;
}