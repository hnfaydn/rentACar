package com.turkcell.rentACar.business.requests.individualCustomerRequests;

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
public class UpdateIndividualCustomerRequest {

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ ]{2,50}", message = BusinessMessages.IndividualCustomerRequestsMessages.FIRST_NAME_REGEX_MESSAGE)
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ ]{2,50}", message = BusinessMessages.IndividualCustomerRequestsMessages.LAST_NAME_REGEX_MESSAGE)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}", message = BusinessMessages.IndividualCustomerRequestsMessages.NATIONAL_IDENTITY_REGEX_MESSAGE)
    private String nationalIdentity;
}