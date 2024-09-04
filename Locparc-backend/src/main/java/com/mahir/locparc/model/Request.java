package com.mahir.locparc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.OrderView;
import com.mahir.locparc.view.RequestView;
import com.mahir.locparc.view.UserView;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    public Request(boolean approved) {
        this.approved = approved;
    }

    public Request(boolean approved, LocalDate responseDate) {
        this.approved = approved;
        this.responseDate = responseDate;
    }

    public Request(boolean approved, LocalDate responseDate, String motive, Order order, User admin) {
        this.approved = approved;
        this.responseDate = responseDate;
        this.motive = motive;
        this.order = order;
        this.admin = admin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({RequestView.class, UserView.class, OrderView.class })
    private Long id;

    @JsonView({RequestView.class, UserView.class, OrderView.class})
    private boolean approved;

    @JsonView({RequestView.class, UserView.class, OrderView.class})
    private LocalDate responseDate;

    @JsonView({RequestView.class, UserView.class, OrderView.class})
    private String motive;

    @OneToOne(mappedBy = "request")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JsonView({RequestView.class})
    private User admin;

}
