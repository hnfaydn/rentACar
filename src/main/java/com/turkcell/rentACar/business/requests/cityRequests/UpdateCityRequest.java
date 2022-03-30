package com.turkcell.rentACar.business.requests.cityRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCityRequest {

    @Min(1)
    private int cityId;

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ]{2,50}", message = BusinessMessages.CityRequestsMessages.COLOR_NAME_REGEX_MESSAGE)
    private String cityName;
}
