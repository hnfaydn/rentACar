package com.turkcell.rentACar.api.models;

import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDelayedPaymentModel {

    @Min(1)
    private int rentalCarId;

    private LocalDate delayedReturnDate;

    @Min(0)
    private double carDelayedKilometerInformation;

    @Valid
    private CreatePaymentRequest createPaymentRequest;
}
