package com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalServiceListDto {

    private int orderedAdditionalServiceId;

    private int customerId;

    private int additionalServiceId;

    private int rentalCarId;
}
