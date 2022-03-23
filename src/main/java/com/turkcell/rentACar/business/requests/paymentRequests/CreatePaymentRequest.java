package com.turkcell.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {


    private String cardNo;

    private String month;

    private String year;

    private String cvv;

}
