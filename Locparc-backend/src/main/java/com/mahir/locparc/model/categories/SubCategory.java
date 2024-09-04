package com.mahir.locparc.model.categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.model.Item;
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
@Table(name = "sub_categories")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    @JsonView({ItemView.class, SubCategoryView.class, OrderView.class, CategoryView.class})
    private String name;

    @ManyToOne
    @JsonView({SubCategoryView.class})
    private Category category;

    @OneToMany(mappedBy = "subCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
        ", category=" + category.getName() +
                '}';
    }
}
