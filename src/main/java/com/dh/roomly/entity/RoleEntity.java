package com.dh.roomly.entity;

import com.dh.roomly.common.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", length = 100, nullable = false)
    private RoleEnum name;

    @Column(name = "DESCRIPTION", length = 45, nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_grant",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "GRANT_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID","GRANT_ID"})
    )
    private Set<PermissionEntity> grant = new HashSet<>();


}
