package com.turkcell.rentACar.business.dtos.rentalCarDtos;

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
}