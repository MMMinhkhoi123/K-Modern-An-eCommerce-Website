package com.leventsclone.leventsclone.entity;

import com.leventsclone.leventsclone.entity.relationship.RelationShipUserVoucher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@IdClass(RelationShipUserVoucher.class)
public class UserVoucher implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "idVoucher")
    private Voucher voucher;

    @Column
    private int quantity;
}
