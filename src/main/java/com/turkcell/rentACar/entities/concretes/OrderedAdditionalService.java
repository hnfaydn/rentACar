package com.turkcell.rentACar.entities.concretes;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordered_additional_services")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class OrderedAdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_additional_services_id")
    private int orderedAdditionalServiceId;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_service_id")
    private AdditionalService additionalService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_car_id")
    private RentalCar rentalCar;
}
