package com.turkcell.rentACar.business.requests.orderedAddtionalRequests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderedAdditionalServiceRequest {

    private int customerId;

    private int additionalServiceId;

    private int rentalCarId;
}
