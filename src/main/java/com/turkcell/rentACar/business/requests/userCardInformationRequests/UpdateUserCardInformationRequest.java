package com.turkcell.rentACar.business.requests.userCardInformationRequests;

import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCardInformationRequest {

    private CreatePaymentRequest paymentInformations;

    @Min(1)
    private int customerId;
}
