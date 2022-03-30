package com.turkcell.rentACar.business.requests.corporateCustomerRequests;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCorporateCustomerRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ 0-9]{2,50}", message = BusinessMessages.CorporateCustomerRequestsMessages.COMPANY_NAME_REGEX_MESSAGE)
    private String companyName;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}", message = BusinessMessages.CorporateCustomerRequestsMessages.TAX_NUMBER_REGEX_MESSAGE)
    private String taxNumber;
}
