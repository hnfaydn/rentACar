package com.turkcell.rentACar.business.dtos.rentalCarDtos;

import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCarListDto {

    private int rentalCarId;

    private LocalDate rentDate;

    private LocalDate returnDate;

    private int rentCityId;

    private int returnCityId;

    private double rentStartKilometer;

    private double returnKilometer;

    private CarDto carDto;

    private CustomerDto customerDto;

    private Integer orderedAdditionalServiceId;

    private OrderedAdditionalServiceDto orderedAdditionalServices;
}
