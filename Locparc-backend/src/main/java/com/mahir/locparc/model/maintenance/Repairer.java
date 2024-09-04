package com.mahir.locparc.model.maintenance;


import com.mahir.locparc.model.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Repairer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 15)
    private String phone;

    @Column(length = 50)
    private String email;

    @ManyToOne
    private Address address;

}
