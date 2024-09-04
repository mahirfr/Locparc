package com.mahir.locparc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ça nous permet d'avoir le champs id autoincrémenté
    private Integer id;

    private String name;

    public Role(int id) {
        this.id = id;
    }
}
