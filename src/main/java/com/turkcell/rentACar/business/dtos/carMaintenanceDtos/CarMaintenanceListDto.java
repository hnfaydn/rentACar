package com.turkcell.rentACar.business.dtos.carMaintenanceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceListDto {

    private int carMaintenanceId;

    private String carMaintenanceDescription;
    private LocalDate returnDate;

    private int carCarId;

}
