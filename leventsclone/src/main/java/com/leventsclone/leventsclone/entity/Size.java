package com.leventsclone.leventsclone.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSize;

    @Column
    private String nameSize;

    @ManyToOne
    @JoinColumn(name="typeSize", nullable=false)
    private TypeSize typeSize;

    @OneToMany(mappedBy = "size")
    private List<OptionSize> optionSize;

}
