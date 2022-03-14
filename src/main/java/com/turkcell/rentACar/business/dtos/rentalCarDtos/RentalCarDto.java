package com.turkcell.rentACar.business.dtos.rentalCarDtos;

import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCarDto {

    private LocalDate rentDate;
    private LocalDate returnDate;

    private int carCarId;

    private OrderedAdditionalServiceDto orderedAdditionalServiceDto;

}
