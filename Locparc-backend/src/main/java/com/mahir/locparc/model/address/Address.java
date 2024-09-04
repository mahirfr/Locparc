package com.mahir.locparc.model.address;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.AddressView;
import com.mahir.locparc.view.OrderView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    public Address(String street,
                   String streetNumber,
                   String addressDetails,
                   String city,
                   String postalCode,
                   Country country) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.addressDetails = addressDetails;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    @JsonView({AddressView.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonView({AddressView.class, OrderView.class})
    @Column(length = 150)
    private String street;

    @JsonView({AddressView.class, OrderView.class})
    @Column(length = 10)
    private String streetNumber;

    @JsonView({AddressView.class, OrderView.class})
    @Column(length = 100)
    private String addressDetails;

    @JsonView({AddressView.class, OrderView.class})
    @Column(length = 50)
    private String city;

    @JsonView({AddressView.class, OrderView.class})
    @Column(length = 10)
    private String postalCode;

    @JsonView({AddressView.class, OrderView.class})
    @ManyToOne
    private Country country;
}
