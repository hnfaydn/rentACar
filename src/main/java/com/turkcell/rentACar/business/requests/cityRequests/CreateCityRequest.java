package com.turkcell.rentACar.business.requests.cityRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCityRequest {

    @Min(1)
    private int cityId;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zi]{2,50}", message = BusinessMessages.CityRequestsMessages.COLOR_NAME_REGEX_MESSAGE)
    private String cityName;
}
