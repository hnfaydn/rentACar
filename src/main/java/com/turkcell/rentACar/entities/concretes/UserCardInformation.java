package com.turkcell.rentACar.entities.concretes;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_card_informations")
public class UserCardInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_card_information_id")
    private int userCardInformationId;

    @Column(name = "card_no",length = 16)
    private String cardNo;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "expiration_month")
    private int expirationMonth;

    @Column(name = "expiration_year")
    private int expirationYear;

    @Column(name = "cvv")
    private int cvv;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

