package com.dh.roomly.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "NAMES")
    private String firstName;

    @Column(nullable = false, name = "LAST_NAMES")
    private String lastName;

    @Column(nullable = false, name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column( nullable = false, name = "IDENTIFICATION_TYPE")
    private Short typeId;

    @Column(unique = true, nullable = false, name = "USERNAME")
    private String username;

    @Column(unique = true, nullable = false, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD_HASH")
    private String password;

    @Column(nullable = false, name = "PHONE")
    private String phoneNumber;

    @Column(nullable = false, name = "CITY")
    private String city;

    @Column(name = "PROFILE_PHOTO_ID")
    private Long profilePhotoId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","rol_id"})
    )
    @Column(nullable = false, name = "ROLE_ID")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(nullable = false, name = "ENABLED")
    private boolean isEnabled;
    @Column(nullable = false, name = "LOCKED")
    private boolean isLocked;
    @Column(nullable = false, name = "ACCOUNT_NON_EXPIRED")
    private boolean accountNonExpired;
    @Column(nullable = false, name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;

    @Transient
    private boolean isAdmin = false;

}
