package com.turkcell.rentACar.business.requests.customerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    private String firstName;

    private String lastName;
}
