package com.turkcell.rentACar.business.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceDto {


    private String carMaintenanceDescription;
    private Date returnDate;

    private int carCarId;
}
