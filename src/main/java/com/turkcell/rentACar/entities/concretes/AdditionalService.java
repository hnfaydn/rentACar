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
@Table(name = "additional_services")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "Lazy"})
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_service_id")
    private int additionalServiceId;

    @Column(name = "additional_service_name")
    private String name;

    @Column(name = "additional_service_dailyPrice")
    private double dailyPrice;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderedAdditionalService> orderedAdditionalServices;
}
