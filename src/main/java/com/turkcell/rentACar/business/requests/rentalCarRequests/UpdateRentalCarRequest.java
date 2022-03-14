package com.turkcell.rentACar.business.requests.rentalCarRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentalCarRequest {

    @NotNull
    private LocalDate rentDate;

    @Nullable
    private LocalDate returnDate;

    @NotNull
    @Min(1)
    private int rentCityId;

    @NotNull
    @Min(1)
    private int returnCityId;

    @NotNull
    @Min(1)
    private int carCarId;

    private List<Integer> additionalServices;
}
