package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class TypeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeProduct;

    @Column
    private String nameTypeProduct;

    @OneToMany(mappedBy = "typeProduct")
    private Set<Product>  products = new HashSet<>();

    @Column
    private String keyTypeProduct;

    @ManyToOne
    @JoinColumn(name = "idDirectory", nullable = false)
    private  Directory directory;

}
