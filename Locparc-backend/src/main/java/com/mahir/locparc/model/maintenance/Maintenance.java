package com.mahir.locparc.model.maintenance;

import com.mahir.locparc.model.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dateSent;

    private LocalDateTime dateReceived;

    private String incident;

    private String incidentImagePath;

    private Double repairCost;

    @ManyToOne
    private Repairer repairer;

    @ManyToOne
    private Item item;

}
