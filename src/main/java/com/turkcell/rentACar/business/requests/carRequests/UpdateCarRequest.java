package com.turkcell.rentACar.business.requests.carRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

    @NotNull
    @Min(0)
    private double dailyPrice;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{2,100}", message = BusinessMessages.CarRequestsMessages.CAR_DESCRIPTION_REGEX_MESSAGE)
    private String description;

    @NotNull
    @Min(0)
    private double kilometerInformation;

    @Nullable
    private List<Integer> carDamageIds;

}
