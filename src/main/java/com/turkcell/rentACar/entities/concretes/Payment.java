package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "month")
    private String month;

    @Column(name = "year")
    private String year;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "payment_date")
    private Date paymentDate;

    @OneToOne
    @JoinColumn(name = "rental_car_id")
    private RentalCar rentalCar;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
