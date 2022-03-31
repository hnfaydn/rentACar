package com.turkcell.rentACar.business.dtos.rentalCarDtos;

import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCarListDto {

    private int rentalCarId;

    private LocalDate rentDate;

    private LocalDate returnDate;

    private CityDto rentCityId;

    private CityDto returnCityId;

    private double rentStartKilometer;

    private double returnKilometer;

    private CarDto carDto;

    private CustomerDto customerDto;

    private List<AdditionalServiceDto> additionalServices;
}
