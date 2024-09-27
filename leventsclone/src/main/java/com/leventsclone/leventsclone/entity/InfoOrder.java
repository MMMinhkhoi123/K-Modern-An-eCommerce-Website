package com.leventsclone.leventsclone.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InfoOrder {

    @Id
    @OneToOne
    private Orders orders;

    @Column
    private String phoneOrder;

    @Column
    private String lastNameOrder;

    @Column
    private String firstName;

    @Column
    private String addressOrder;

    @Column
    private String emailNotifyOrder;

    @Column
    private String suitOrder;

    @Column
    private String phoneExtraOrder;

    @Column
    private String typeTransportOrder;

    @Column
    private String typePaymentOrder;

    @Column
    private boolean packagingOrder;

    @Column
    private String contentPackagingOrder;
}
