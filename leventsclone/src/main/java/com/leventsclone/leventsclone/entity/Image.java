package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column
    private String link;

    @Column
    private String code;

    @OneToMany(mappedBy = "image")
    private List<OptionImage> productOptionImages;

    @ManyToMany(mappedBy = "images")
    private List<Outfit> outfits;

    @OneToMany(mappedBy = "image")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "image")
    private List<Rating> ratings;
}
