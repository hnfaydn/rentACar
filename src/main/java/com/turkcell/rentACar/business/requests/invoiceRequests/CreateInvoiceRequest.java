package com.turkcell.rentACar.business.requests.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {

    @Min(1)
    private int rentalCarId;

}
