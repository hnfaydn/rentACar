package com.turkcell.rentACar.business.dtos.customerDtos;

import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListDto {

    private int customerId;

    private String firstName;

    private String lastName;

    private double additionalServicePayment;

    private double totalPayment;

    private List<OrderedAdditionalService> orderedAdditionalServices;
}
