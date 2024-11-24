package com.dh.roomly.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity {

    @EmbeddedId
    private ReviewId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @MapsId("propertyId")
    @JoinColumn(name = "PROPERTY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private PropertyEntity property;

    @Column(name = "RATING", nullable = false)
    private byte rating;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;
}
