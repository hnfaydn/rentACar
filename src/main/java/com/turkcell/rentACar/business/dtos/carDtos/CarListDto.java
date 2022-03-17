package com.turkcell.rentACar.business.dtos.carDtos;

import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarListDto {

    private int carId;

    private double dailyPrice;
    private int modelYear;
    private String description;
    private double kilometerInformation;
    private String brandId;
    private String colorId;

    private CarDamageListDto carDamageListDto;

}
