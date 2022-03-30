package com.turkcell.rentACar.entities.concretes;

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
@Table(name = "cars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private int carId;

    @Column(name = "car_daily_price")
    private double dailyPrice;

    @Column(name = "car_model_year")
    private int modelYear;

    @Column(name = "car_description")
    private String description;

    @Column(name = "kilometer_information")
    private double kilometerInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarMaintenance> carMaintenances;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalCar> rentalCars;


    @ManyToMany
    @JoinTable(name = "cars_car_damages",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "car_damage_id")}
    )
    private List<CarDamage> carDamages;


}
