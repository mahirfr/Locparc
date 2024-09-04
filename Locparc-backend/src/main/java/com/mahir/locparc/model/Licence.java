package com.mahir.locparc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "licence")
public class Licence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(length = 300)
    private String description;

    private Integer maxNumber;

    @ManyToMany
    @JoinTable(name = "item_licences",
               joinColumns =        @JoinColumn(name = "licence_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id"   ))
    private Set<Item> items = new HashSet<>();

}
