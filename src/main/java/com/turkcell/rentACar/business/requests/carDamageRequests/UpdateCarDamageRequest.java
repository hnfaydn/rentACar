package com.turkcell.rentACar.business.requests.carDamageRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarDamageRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2,100}", message = BusinessMessages.CarDamageRequestsMessages.CAR_DAMAGE_DESCRIPTION_REGEX_MESSAGE)
    private String damageDescription;
}
