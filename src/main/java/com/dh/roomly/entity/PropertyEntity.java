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
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Column(name = "DESCRIPTION", length = 100, nullable = false)
    private String description;

    @Column(name = "PRICE_PER_NIGHT", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "EXACT_ADDRESS", nullable = false)
    private String exactAddress;

    @Column(name = "MAX_CAPACITY", nullable = false)
    private Short maxCapacity;

    @Column(name = "NUM_ROOMS", nullable = false)
    private Short numRooms;

    @Column(name = "NUM_BEDS", nullable = false)
    private Short numBeds;

    @Column(name = "NUM_BATHROOMS", nullable = false)
    private Short numBathrooms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private UserEntity owner;

    @Column(name = "CATEGORY_ID", nullable = false)
    private Short categoryId;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "id",
            insertable = false, updatable = false)
    private CategoryEntity category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "property_photo",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<FileEntity> photos = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIN_PHOTO_ID", referencedColumnName = "ID")
    private FileEntity mainPhoto;

    @ManyToOne
    @JoinColumn(name = "CITY_ID", nullable = false)
    private CityEntity city;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "property_feature",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<FeatureEntity> features = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BookingEntity> bookings;
}
