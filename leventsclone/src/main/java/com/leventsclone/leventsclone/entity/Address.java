package com.leventsclone.leventsclone.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAddress;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User userAddress;
    @Column
    private String lastNameAddress;
    @Column
    private String FirstNameAddress;
    @Column
    private String PhoneAddress;
    @Column
    private String cityAddress;
    @Column
    private String districtAddress;
    @Column
    private String communeAddress;
    @Column
    private String detailOverAddress;
    @Column
    private boolean usingAddress;
}
