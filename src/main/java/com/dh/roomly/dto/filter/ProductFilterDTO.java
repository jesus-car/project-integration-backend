package com.dh.roomly.dto.filter;

import com.dh.roomly.common.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterDTO{
    private String name;
    private String description;
    private BigDecimal pricePerNight;
    private BigDecimal minPricePerNight;
    private BigDecimal maxPricePerNight;
    private Short cityId;
    private String exactAddress;
    private Short maxCapacity;
    private Short numRooms;
    private Short minNumRooms;
    private Short maxNumRooms;
    private Short numBeds;
    private Short minNumBeds;
    private Short maxNumBeds;
    private Short numBathrooms;
    private Short minNumBathrooms;
    private Short maxNumBathrooms;
    private Long ownerId;
    private Short categoryId;
}
