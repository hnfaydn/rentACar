package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@Table(name="customers")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
@PrimaryKeyJoinColumn(name="customer_id",referencedColumnName = "user_id")
public class Customer extends User {

    @Column(name = "customer_id", insertable = false , updatable = false)
    private int customerId;

    @Column(name="registration_date")
    private LocalDate registrationDate;


    @OneToMany(mappedBy = "customer")
    private List<RentalCar> rentalCars;

}
