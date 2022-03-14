package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rental_cars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class RentalCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_car_id")
    private int rentalCarId;

    @Column(name = "rent_date")
    private LocalDate rentDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "city_id",insertable = false,updatable = false)
    private City rentCityId;

    @ManyToOne
    @JoinColumn(name = "city_id",insertable = false,updatable = false)
    private City returnCityId;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToOne
    private OrderedAdditionalService orderedAdditionalService;

}
