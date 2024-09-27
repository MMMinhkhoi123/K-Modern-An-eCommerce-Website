package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMember;

    @Column
    private String nameMember;

    @Column
    private double priceAchieveMember;

    @Column
    private int gradeMember;

    @Column
    private int percentDiscountMember;

    @OneToMany(mappedBy = "member")
    private List<User> users = new ArrayList<>();

}
