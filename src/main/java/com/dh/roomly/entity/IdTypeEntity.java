package com.dh.roomly.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "identification_type")
public class IdTypeEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "ID")
    private Short id;
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;
}
