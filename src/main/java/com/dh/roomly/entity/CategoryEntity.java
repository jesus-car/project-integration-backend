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
    @Column(name = "TITLE", nullable=false, length=45)
    private String title;
    @Column(name = "DESCRIPTION", length=100, nullable = false)
    private String description;
    @OneToOne
    @JoinColumn(name = "FILE_ID", referencedColumnName = "id")
    private FileEntity file;
}
