package com.leventsclone.leventsclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRole;
    @Column(unique = true)
    private String nameRole;

    @ManyToMany(mappedBy="roles")
    private List<User> users;
}
