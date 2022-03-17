package com.turkcell.rentACar.business.requests.rentalCarRequests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalCarRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rentDate;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @NotNull
    @Min(1)
    private int rentCityId;

    @NotNull
    @Min(1)
    private int returnCityId;

    @Min(0)
    private double returnKilometer;

    @NotNull
    @Min(1)
    private int carCarId;

    @NotNull
    @Min(1)
    private int customerId;

    @Nullable
    private Integer orderedAdditionalServiceId;


}
