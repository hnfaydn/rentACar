package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turkcell.rentACar.core.utilities.entities.concretes.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
@PrimaryKeyJoinColumn(name="customer_id",referencedColumnName = "user_id")
public class Customer extends User {

    @Column(name = "customer_id",insertable = false,updatable = false)
    private int customerId;

    @Column(name = "additional_service_total_payment")
    private double additionalServicePayment;

    @Column(name = "customer_total_payment")
    private double totalPayment;

    @OneToMany(mappedBy = "additionalService", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderedAdditionalService> orderedAdditionalServices;

}
