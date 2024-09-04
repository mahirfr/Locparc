package com.mahir.locparc.model.manufacturers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.model.Item;
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
@Table(name = "models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ModelView.class})
    private Long id;

    @Column(nullable = false, length = 50)
    @JsonView({OrderView.class, ItemView.class, ModelView.class, ManufacturerView.class})
    private String reference;

    @ManyToOne
    @JsonView({ModelView.class})
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Item> items = new HashSet<>();


}
