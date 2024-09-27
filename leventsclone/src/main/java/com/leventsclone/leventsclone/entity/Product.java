package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    @Column
    private double priceProduct;

    @Column
    private String nameProduct;

    @Column(length = 65555)
    private String describeProduct;

    @Column
    private boolean classicProduct;

    @ManyToOne
    @JoinColumn(name="typeProduct", nullable=false)
    private TypeProduct typeProduct;

    @OneToMany(mappedBy = "product")
    private Set<Option> options;

    @ManyToMany(mappedBy = "products")
    List<Collection> listCollectionProduct = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    private EventProduct event;
}


