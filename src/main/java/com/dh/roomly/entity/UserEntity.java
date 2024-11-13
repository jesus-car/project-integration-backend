package com.dh.roomly.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "USERNAME")
    private String username;

    @Column(nullable = false, name = "NAMES", length = 45)
    private String firstName;

    @Column(nullable = false, name = "LAST_NAMES", length = 45)
    private String lastName;

    @Column(nullable = false, name = "IDENTIFICATION_NUMBER", length = 25)
    private String identificationNumber;

    @OneToOne
    @JoinColumn(name = "IDENTIFICATION_TYPE_ID")
    private IdTypeEntity typeId;

    @Column(unique = true, nullable = false, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD_HASH")
    private String password;

    @Column(nullable = false, name = "PHONE", length = 45)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "CITY_ID", nullable = false)
    private CityEntity city;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_PHOTO_ID")
    private FileEntity profilePhoto;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private RoleEntity role;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(nullable = false, name = "IS_ENABLED")
    private boolean isEnabled;
    @Column(nullable = false, name = "ACCOUNT_NON_EXPIRED")
    private boolean accountNonExpired;
    @Column(nullable = false, name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;
    @Column(nullable = false, name = "ACCOUNT_NON_LOCKED")
    private boolean accountNonLocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Transient
    private boolean isSeller;
    @Transient
    private boolean isAdmin;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyEntity> properties;

}
