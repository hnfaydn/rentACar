package com.turkcell.rentACar.business.dtos.customerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private int customerId;

    private String email;
}
