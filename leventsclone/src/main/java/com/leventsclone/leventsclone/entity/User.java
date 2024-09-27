package com.leventsclone.leventsclone.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    @Column(nullable = false)
    private String nameUser;

    @Column(nullable = false, unique = true)
    private String phoneUser;

    @Column(nullable = false, unique = true)
    private String emailUser;

    @Column
    private Date  birthdayUser;

    @Column
    private String genderUser;

    @Column
    private String passwordUser;

    @Column
    private int gradeSetMemberUser;

    @Column
    private int gradeChangeRewardUser;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName="idUser")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="idRole")})
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserVoucher> userVouchers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "userAddress")
    private List<Address> addresses = new ArrayList<>();

    @ManyToOne
    private Member member;

    @Column(unique = true)
    private Date expireForGetPassUser;

    @Column
    private String keyForGetPassUser;

    @Column
    private String tokenFormPassUser;

    @Column
    private String stateResetPassUser;

    @CreatedDate
    private Date createdDate;

    @OneToMany(mappedBy = "user")
    private List<UserWishList> userWishLists = new ArrayList<>();
}
