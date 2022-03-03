package com.turkcell.rentACar.entities.concretes;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int carId;


    @Column(name = "daily_price")
    private double dailyPrice;

    @Column(name = "model_year")
    private int modelYear;


    @Column(name = "description")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;


    @ManyToOne(fetch = FetchType.LAZY)
    private Color color;


    @ManyToOne(fetch = FetchType.LAZY)
    private CarMaintenance carMaintenance;

}
