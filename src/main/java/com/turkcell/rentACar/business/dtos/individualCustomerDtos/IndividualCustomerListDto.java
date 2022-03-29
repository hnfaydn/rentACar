package com.turkcell.rentACar.business.dtos.individualCustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCustomerListDto {

    private int customerId;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String nationalIdentity;
}
