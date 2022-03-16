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
@Table(name = "cities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private int cityId;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "rentCity")
    private List<RentalCar> rentalCarRentCity;

    @OneToMany(mappedBy = "returnCity")
    private List<RentalCar> rentalCarReturnCity;

}
