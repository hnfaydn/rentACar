package com.turkcell.rentACar.business.requests.rentalCarRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalCarRequest {

    @NotNull
    private LocalDate rentDate;
    @Nullable
    private LocalDate returnDate;

    @NotNull
    @Min(1)
    private int carCarId;
}
