package com.turkcell.rentACar.business.requests.colorRequests;


import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateColorRequest {

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ 0-9]{2,50}", message = BusinessMessages.ColorRequestsMessages.COLOR_NAME_REGEX_MESSAGE)
    private String name;
}
