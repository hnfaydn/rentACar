package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "invoice_number",unique = true)
    private Integer invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "additional_service_total_payment")
    private Double additionalServiceTotalPayment;

    @Column(name = "rent_day")
    private Integer rentDay;

    @Column(name = "rent_payment")
    private Double rentPayment;

    @Column(name = "rent_location_payment")
    private Double rentLocationPayment;

    @Column(name = "total_payment")
    private Double totalPayment;

    @OneToOne()
    @JoinColumn(name = "rental_car_id")
    private RentalCar rentalCar;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}