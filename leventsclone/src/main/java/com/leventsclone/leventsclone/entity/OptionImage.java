package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OptionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column
    private Boolean isActive;

}
