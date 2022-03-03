package com.turkcell.rentACar.business.dtos;

import com.turkcell.rentACar.entities.concretes.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceDto {

    private String description;
    private String returnDate;

    private int carId;

}
