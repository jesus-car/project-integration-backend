package com.dh.roomly.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "property")
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @Column(name = "PRICE_PER_NIGHT", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "CITY_ID", nullable = false)
    private Short cityId;

    @Column(name = "EXACT_ADDRESS", length = 256)
    private String exactAddress;

    @Column(name = "MAX_CAPACITY", nullable = false)
    private Short maxCapacity;

    @Column(name = "NUM_ROOMS", nullable = false)
    private Short numRooms;

    @Column(name = "NUM_BEDS", nullable = false)
    private Short numBeds;

    @Column(name = "NUM_BATHROOMS", nullable = false)
    private Short numBathrooms;

    @Column(name = "OWNER_ID", nullable = false)
    private Long ownerId;

    @Column(name = "CATEGORY_ID", nullable = false)
    private Short categoryId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "property_photo",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<FileEntity> photos = new ArrayList<>();
}
