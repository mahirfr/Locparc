package com.mahir.locparc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.model.categories.SubCategory;
import com.mahir.locparc.model.maintenance.Maintenance;
import com.mahir.locparc.model.manufacturers.Model;
import com.mahir.locparc.view.ItemView;
import com.mahir.locparc.view.OrderView;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {
    public Item(String name) {
        this.name = name;
    }

    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(String name, boolean existing) {
        this.name = name;
        this.existing = existing;
    }

    public Item(String name, boolean existing, boolean onMaintenance) {
        this.name = name;
        this.existing = existing;
        this.onMaintenance = onMaintenance;
    }

    public Item(String name, boolean existing, boolean onMaintenance, Set<OrderItem> orders) {
        this.name = name;
        this.existing = existing;
        this.onMaintenance = onMaintenance;
        this.orders = orders;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({OrderView.class, ItemView.class})
    private Long id;

    @Column(length = 255, unique = true)
    @JsonView({OrderView.class, ItemView.class})
    private String serialNumber;

    private Date arrivalDate;

    @Column(length = 50)
    @JsonView({OrderView.class, ItemView.class})
    private String name;

    @JsonView({OrderView.class, ItemView.class})
    private String description;

    @JsonView({OrderView.class, ItemView.class})
    private boolean existing;

    @JsonView({OrderView.class, ItemView.class})
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private int quantity;

    @JsonView({OrderView.class, ItemView.class})
    private Double pricePerDay;

    @JsonView({OrderView.class, ItemView.class})
    private Date warranty;

    @JsonView({OrderView.class, ItemView.class})
    private boolean onMaintenance;

    @JsonView({OrderView.class, ItemView.class})
    @Column(length = 512)
    private String imageUrl;

    @ManyToOne
    @JsonView({OrderView.class, ItemView.class})
    private SubCategory subCategory;

    @ManyToOne
    @JsonView({OrderView.class, ItemView.class})
    private Model model;

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private Set<Licence> licences = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Set<OrderItem> orders = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Set<Maintenance> maintenances = new HashSet<>();


}
