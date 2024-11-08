package com.dh.roomly.entity;

import com.dh.roomly.dto.IDTOEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "city")
public class CityEntity implements IDTOEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;
}
