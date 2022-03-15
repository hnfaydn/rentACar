package com.turkcell.rentACar.business.requests.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCorporateCustomerRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 2,max = 30)
    private String companyName;

    @NotNull
    @Size(min = 2,max = 30)
    private String taxNumber;
}
