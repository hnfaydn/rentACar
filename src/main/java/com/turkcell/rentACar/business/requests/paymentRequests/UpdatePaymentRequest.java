package com.turkcell.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequest {

    @Pattern(regexp="^[0-9]{16}",message="length must be 16 and all digits have to be an integer")
    private String cardNo;

    @Pattern(regexp="^[a-zA-Z]{5,50}",message="Card holder input have to consist of letters and size have to between 5 and 50! ")
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
