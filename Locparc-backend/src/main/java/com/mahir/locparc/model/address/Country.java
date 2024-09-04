package com.mahir.locparc.model.address;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.CountryView;
import com.mahir.locparc.view.OrderView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {

    public Country(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonView({OrderView.class})
    private String name;

}
