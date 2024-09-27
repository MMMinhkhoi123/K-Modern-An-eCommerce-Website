package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity(name = "optionOriginal")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idColor")
    private Color color;

    @OneToMany(mappedBy = "option")
    private Set<OptionImage> optionImages = new HashSet<>();

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private Set<OptionSize> optionSize = new HashSet<>();

    @Column
    private int price;

    @Column
    @CreatedDate
    private Date dataCreate;

    @ManyToMany(mappedBy = "options")
    private List<Outfit> outfits = new LinkedList<>();

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<Rating> ratings = new LinkedList<>();

    @OneToMany(mappedBy = "option")
    private List<UserWishList> userWishLists = new ArrayList<>();

}
