package com.turkcell.rentACar.entities.concretes;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_maintenances")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","Lazy"})
public class CarMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_maintenance_id")
    private int carMaintenanceId;

    @Column(name = "car_maintenance_description")
    private String carMaintenanceDescription;

    @Column(name = "return_date")
    private Date returnDate;

    @ManyToOne
    @JoinColumn(name="car_id")
    private Car car;
}
