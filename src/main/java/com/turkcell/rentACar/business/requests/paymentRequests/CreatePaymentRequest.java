package com.turkcell.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;


import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {


    @Pattern(regexp="^[0-9]{16}",message="length must be 16 and all digits have to be an integer")
    private String cardNo;

    @Pattern(regexp="^[a-zA-Z]{5,50}",message="Card holder input have to consist of letters and size have to between 5 and 50! ")
    private String cardHolder;

    @Range(min = 1,max = 12)
    private int expirationMonth;

    @Range(min = 2022,max = 2032)
    private int expirationYear;

    @Range(min = 100,max = 999)
    private int cvv;

}
