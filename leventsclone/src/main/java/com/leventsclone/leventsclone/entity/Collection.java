package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCollection;

    @Column
    private String nameCollection;

    @Column
    private boolean darkColorCollection;

    @Column(length = 65555)
    private String describeCollection;

    @Column
    private String imageCollection;

    @Column
    private String keyCollection;

    @ManyToMany
    @JoinTable(
            name = "Products_Collections",
            joinColumns = @JoinColumn(name = "idCollecton"),
            inverseJoinColumns = @JoinColumn(name = "idProduct"))
    private List<Product> products = new ArrayList<>();
}
