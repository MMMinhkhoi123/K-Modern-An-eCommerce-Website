package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    @Column
    private String nameEvent;

    @Column(unique = true)
    private String pathEvent;

    @Column
    private Date dateStartEvent;

    @Column
    private Date dateStartEndEvent;

    @Column
    private Boolean stateEvent;

    @OneToMany(mappedBy = "eventEventProduct",cascade = CascadeType.ALL)
    private List<EventProduct> eventProduct = new ArrayList<>();
}
