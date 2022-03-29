package com.turkcell.rentACar.business.requests.carRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-zA-Z0-9]{2,100}", message = BusinessMessages.CarRequestsMessages.CAR_DESCRIPTION_REGEX_MESSAGE)
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

    @Nullable
    private List<Integer> carDamageIds;

}
