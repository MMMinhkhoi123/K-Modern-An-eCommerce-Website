package com.leventsclone.leventsclone.entity;


import com.leventsclone.leventsclone.entity.relationship.RelationShipUserProductColor;
import com.leventsclone.leventsclone.entity.relationship.RelationShipUserVoucher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class UserWishList implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private  User user;

    @ManyToOne
    private Option option;

}
