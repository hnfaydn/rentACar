package com.turkcell.rentACar.business.requests.userCardInformationRequests;

import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCardInformationRequest {

    private CreatePaymentRequest paymentInformations;

    private int customerId;
}
