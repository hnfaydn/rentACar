package com.turkcell.rentACar.business.requests.userRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @Email
    private String email;

    private String password;
}
