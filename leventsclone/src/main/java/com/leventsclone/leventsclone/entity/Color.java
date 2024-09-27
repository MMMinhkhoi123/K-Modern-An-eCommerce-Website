package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;

    @Column
    private String nameColor;

    @Column
    private String codeColor;

    @OneToMany(mappedBy = "color")
    private Set<Option> options;



}
