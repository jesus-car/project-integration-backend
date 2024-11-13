package com.dh.roomly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Short id;
    @Column(name = "TITLE", unique=true, nullable=false, length=100)
    private String title;
    @Column(name = "DESCRIPTION", length=400)
    private String description;
    @OneToOne
    @JoinColumn(name = "FILE_ID", referencedColumnName = "id")
    private FileEntity file;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyEntity> properties;
}
