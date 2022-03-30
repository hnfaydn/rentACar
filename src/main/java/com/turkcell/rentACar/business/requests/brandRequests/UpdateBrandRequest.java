package com.turkcell.rentACar.business.requests.brandRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBrandRequest {

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ 0-9]{2,50}", message = BusinessMessages.BrandRequestsMessages.BRAND_NAME_REGEX_MESSAGE)
    private String name;
}
