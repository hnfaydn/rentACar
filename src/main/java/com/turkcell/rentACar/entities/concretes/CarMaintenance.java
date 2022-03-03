package com.turkcell.rentACar.entities.concretes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_maintenances")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CarMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int carMaintenanceId;

    @Column(name="description")
    private String description;

    @Column(name="return_date")
    private String returnDate;



    @OneToMany(mappedBy = "carMaintenance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Car> cars;
}
