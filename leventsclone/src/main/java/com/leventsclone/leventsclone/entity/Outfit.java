package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String height;

    @Column
    private String weight;

    @ManyToOne
    private OptionSize optionSize;

    @ManyToMany
    @JoinTable(
            name = "option_original_outfits",
            joinColumns = @JoinColumn(name = "outfits_id"),
            inverseJoinColumns = @JoinColumn(name = "options_id")
    )
    private List<Option> options = new LinkedList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "image_outfits",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "images_id")
    )
    private List<Image> images = new LinkedList<>();
}
