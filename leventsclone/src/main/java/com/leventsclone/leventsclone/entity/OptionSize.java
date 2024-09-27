package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class OptionSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idSize", nullable = false)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "idOption", nullable = false)
    private Option option;

    @OneToMany(mappedBy = "optionSize")
    private List<OrderDetail> orderDetails;

    @Column
    private int quantity;


    @OneToMany(mappedBy = "optionSize")
    private List<Outfit> outfits;


}
