package com.turkcell.rentACar.business.requests.additionalServiceRequests;

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
public class UpdateAdditionalServiceRequest {

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvwqyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVWQYZ 0-9]{2,50}", message = BusinessMessages.AdditionalServiceRequestsMessages.ADDITIONAL_SERVICE_NAME_REGEX_MESSAGE)
    private String additionalServiceName;

    @NotNull
    @Min(0)
    private double additionalServiceDailyPrice;
}
