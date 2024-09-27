package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Option option;

    @Column
    private int count;

    @Column
    private String comment;

    @Column
    private String title;

    @Column
    private Boolean permit;

    @Column
    @CreatedDate
    private Date dateAssess;

    @ManyToOne
    private Image image;

    @OneToOne
    private Orders orders;
}
