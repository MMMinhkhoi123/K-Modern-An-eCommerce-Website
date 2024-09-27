package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUpload;

    @Column
    private String linkUpload;

    @Column
    private String keyUpload;

    @Column
    private Date dateUpload;
}
