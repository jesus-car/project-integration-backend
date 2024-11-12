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
@Table(name = "roles")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleEnum name;

    @Column(name = "description", unique = true)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id","permission_id"})
    )
    private Set<PermissionEntity> permissions = new HashSet<>();


}
