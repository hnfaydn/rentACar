package com.turkcell.rentACar.business.requests.carRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
    @Size(min = 10)
    private String description;

    @NotNull
    @Min(0)
    private double kilometerInformation;

    @NotNull
    @Min(1)
    private int brandId;

    @NotNull
    @Min(1)
    private int colorId;

    private List<Integer> carDamageIds;

}
