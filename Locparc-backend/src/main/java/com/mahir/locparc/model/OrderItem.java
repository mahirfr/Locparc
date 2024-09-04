package com.mahir.locparc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.view.ItemView;
import com.mahir.locparc.view.OrderView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_item")
@IdClass(OrderItem.OrderItemsPK.class)
public class OrderItem {

    @Id
    @JsonView({OrderView.class})
    private Long orderId;

    @Id
    @JsonView({ItemView.class, OrderView.class})
    private Long itemId;

    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("item_id")
    @JoinColumn(name = "item_id")
    @JsonView({OrderView.class})
    private Item item;

    @JsonView({OrderView.class})
    private Date returnDate;

    @JsonView({OrderView.class})
    private boolean approved;


    @Embeddable // Can be used inside another class
    @Getter
    @Setter
    public static class OrderItemsPK implements Serializable {
        @Column(name = "order_id")
        Long orderId;

        @Column(name = "item_id")
        Long itemId;
    }

}
