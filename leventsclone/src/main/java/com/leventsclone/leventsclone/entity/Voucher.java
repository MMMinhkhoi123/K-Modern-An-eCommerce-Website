package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoucher;

    @Column(unique = true)
    private String codeVoucher;

    @Column
    private double priceDiscountVoucher;

    @Column
    private double percentDiscountVoucher;

    @Column
    private double priceConditionVoucher;

    @Column
    private String describeVoucher;

    @Column
    private String typeVoucher;

    @OneToMany(mappedBy="voucher")
    private List<UserVoucher> userVouchers = new ArrayList<>();

    @ManyToMany(mappedBy = "vouchers")
    private  List<Orders> orders = new ArrayList<>();
}
