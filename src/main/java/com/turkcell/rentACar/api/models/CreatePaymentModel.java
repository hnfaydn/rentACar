package com.turkcell.rentACar.api.models;

import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentModel {

    @Valid
    private CreateRentalCarRequest createRentalCarRequest;

    @Valid
    private CreatePaymentRequest createPaymentRequest;
}
