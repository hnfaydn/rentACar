package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "rent_city_id")
    private City rentCity;

    @ManyToOne
    @JoinColumn(name = "return_city_id")
    private City returnCity;

    @Column(name = "rent_start_kilometer")
    private double rentStartKilometer;

    @Column(name = "return_kilometer")
    private double returnKilometer;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToOne
    private OrderedAdditionalService orderedAdditionalService;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
