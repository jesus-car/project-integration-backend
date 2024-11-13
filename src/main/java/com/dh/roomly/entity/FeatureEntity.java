package com.dh.roomly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feature")
public class FeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "SMALLINT")
    private Short id;

    @Column(name = "NAME", length = 45, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "ICON_NAME", length = 45)
    private String iconName;
}
