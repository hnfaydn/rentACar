package com.turkcell.rentACar.business.requests;

import com.turkcell.rentACar.entities.concretes.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarMaintenanceRequest {

    private String description;
    private String returnDate;

    private String carDescription;

}
