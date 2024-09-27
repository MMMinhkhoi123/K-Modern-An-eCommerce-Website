package com.leventsclone.leventsclone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Cookie {
    @Id
    private String idCookie;
    @Column
    private Date expire;
}
