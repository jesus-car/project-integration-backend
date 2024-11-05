package com.dh.roomly.entity;

import com.dh.roomly.dto.IDTOEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "state")
public class StateEntity implements IDTOEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(name = "NAME",unique=true, nullable=false, length=100)
    private String name;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CityEntity> cities;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;
}
