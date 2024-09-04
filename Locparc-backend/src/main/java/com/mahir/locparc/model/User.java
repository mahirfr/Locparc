package com.mahir.locparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.model.address.Address;
import com.mahir.locparc.view.OrderView;
import com.mahir.locparc.view.UserView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // L'annotation qui communique à Spring Boot qu'il s'agit d'une entité
@Getter // Loombok, la dépendence Maven nous génére les getteurs
@Setter // ainsi que les setteurs
public class User {
    @Id // L'entité User a besoin d'un id pour être considéré comme telle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ça nous permet d'avoir le champ id autoincrémenté
    @JsonView({UserView.class})
    private Long id;

    @Column(nullable = false, length = 50)
    @JsonView({UserView.class})
    private String firstName;

    @Column(nullable = false, length = 50)
    @JsonView({UserView.class})
    private String lastName;

    @Column(nullable = false, length = 50, unique = true)
    @JsonView({OrderView.class, UserView.class})
    private String email;

    @Column(length = 15)
    @JsonView({UserView.class})
    private String phone;

    @Column(length = 256)
    private String password;

    private boolean active;

    @ManyToOne
    @JsonView({UserView.class})
    private Address address;

    @Transient
    private String firstPassword;

    @ManyToOne
    @JsonView({UserView.class})
    private Role role;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({UserView.class})
    private List<Request> requests = new ArrayList<>();



}
