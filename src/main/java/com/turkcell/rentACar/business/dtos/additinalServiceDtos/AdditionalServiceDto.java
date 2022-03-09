package com.turkcell.rentACar.business.dtos.additinalServiceDtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalServiceDto {


    private String name;
    private double dailyPrice;
}
