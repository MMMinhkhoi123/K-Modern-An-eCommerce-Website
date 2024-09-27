package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDirectory;

    @Column
    private String nameDirectory;

    @Column
    private String keyDirectory;

    @OneToMany(mappedBy = "directory")
    Set<TypeProduct> typeProducts = new HashSet<>();
}
