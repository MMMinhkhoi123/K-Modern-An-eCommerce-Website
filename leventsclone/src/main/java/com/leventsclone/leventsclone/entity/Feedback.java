package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedback;

    @Column
    private Boolean showFeedback;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Image image;

    @ManyToOne
    private Option option;

}
