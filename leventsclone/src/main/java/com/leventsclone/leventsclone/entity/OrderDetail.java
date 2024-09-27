package com.leventsclone.leventsclone.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_order", nullable=false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name="idOptionSize", nullable=false)
    private OptionSize optionSize;

    private int quantity;

    private int quantityDefect;

    private String note;

    private String state;

    private Integer price;
}
