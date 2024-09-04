package com.mahir.locparc.model.categories;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.CategoryView;
import com.mahir.locparc.view.ItemView;
import com.mahir.locparc.view.OrderView;
import com.mahir.locparc.view.SubCategoryView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CategoryView.class})
    private Integer id;

    @JsonView({ItemView.class, CategoryView.class, OrderView.class, SubCategoryView.class})
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({CategoryView.class})
    private List<SubCategory> subCategories = new ArrayList<>();
}
