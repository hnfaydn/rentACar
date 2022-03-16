package com.turkcell.rentACar.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@EntityListeners({AuditingEntityListener.class})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ordered_additional_services")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderedAdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_additional_service_id")
    private Integer orderedAdditionalServiceId;

    @ManyToMany
    @JoinTable(name = "ordered_additional_services_additional_services",
            joinColumns = {@JoinColumn(name = "ordered_additional_service_id")},
            inverseJoinColumns = {@JoinColumn(name = "additional_service_id")}
    )
    private List<AdditionalService> additionalServices;

    @OneToOne(mappedBy = "orderedAdditionalService")
    private RentalCar rentalCar;


}