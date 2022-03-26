package com.turkcell.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequest {

    @Size(min = 16,max = 16)
    private String cardNo;

    @Size(min = 2,max = 50)
    private String cardHolder;

    @Min(1)
    @Max(12)
    private int expirationMonth;

    @Min(2022)
    @Max(2032)
    private int expirationYear;

    @Min(100)
    @Min(999)
    private int cvv;
}
