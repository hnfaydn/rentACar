package com.turkcell.rentACar.business.dtos.carDtos;

import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    private int carId;

    private double dailyPrice;
    private int modelYear;
    private String description;
    private double kilometerInformation;
    private String brandName;
    private String colorName;

    private List<CarDamageListDto> carDamages;

}
