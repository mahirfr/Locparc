package com.mahir.locparc.model.manufacturers;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.ItemView;
import com.mahir.locparc.view.ManufacturerView;
import com.mahir.locparc.view.ModelView;
import com.mahir.locparc.view.OrderView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ManufacturerView.class})
    private Integer id;

    @Column(nullable = false, length = 50)
    @JsonView({OrderView.class, ItemView.class, ManufacturerView.class, ModelView.class})
    private String name;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({ManufacturerView.class})
    private Set<Model> models = new HashSet<>();

}
