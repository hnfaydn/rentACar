package com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalServiceDto {


    private int customerId;

    private int additionalServiceId;

    private int rentalCarId;

}
