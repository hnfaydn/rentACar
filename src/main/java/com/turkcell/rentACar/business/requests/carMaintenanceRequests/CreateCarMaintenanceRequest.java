package com.turkcell.rentACar.business.requests.carMaintenanceRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarMaintenanceRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2,100}", message = BusinessMessages.CarMaintenanceRequestsMessages.CAR_MAINTENANCE_DESCRIPTION_REGEX_MESSAGE)
    private String carMaintenanceDescription;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @NotNull
    @Min(1)
    private int carCarId;
}
