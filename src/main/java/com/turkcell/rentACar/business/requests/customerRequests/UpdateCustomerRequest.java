package com.turkcell.rentACar.business.requests.customerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerRequest {

    @Email
    private String email;

    private String password;
}
