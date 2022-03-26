package com.turkcell.rentACar.business.requests.carDamageRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarDamageRequest {

    @Size(min = 5, max = 50)
    private String damageDescription;
}
