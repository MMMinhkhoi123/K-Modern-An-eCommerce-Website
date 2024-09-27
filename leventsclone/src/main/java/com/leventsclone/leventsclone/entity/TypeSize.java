package com.leventsclone.leventsclone.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class TypeSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeSize;

    @Column
    private String nameTypeSize;

    @OneToMany(mappedBy="typeSize")
    private Set<Size> sizes;
}
