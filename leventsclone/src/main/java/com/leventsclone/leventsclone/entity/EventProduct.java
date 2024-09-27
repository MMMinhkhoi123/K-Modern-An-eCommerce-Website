package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EventProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEventProduct;

    @ManyToOne
    private Event eventEventProduct;

    @Column
    private int percentDiscount;

    @OneToOne(mappedBy = "event")
    private Product product;


}
