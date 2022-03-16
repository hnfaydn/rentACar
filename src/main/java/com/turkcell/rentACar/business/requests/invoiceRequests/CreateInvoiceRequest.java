package com.turkcell.rentACar.business.requests.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {

    private int rentalCarId;

}
