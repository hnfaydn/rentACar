package com.turkcell.rentACar.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {

    @NotNull
    @Min(0)
    private double dailyPrice;

    @NotNull
    @Min(1900)
    private int modelYear;

    @NotNull
    @Size(min=10)
    private String description;

    @NotNull
    @Min(1)
    private int brandId;

    @NotNull
    @Min(1)
    private int colorId;



}
