package com.turkcell.rentACar.business.dtos.userCardInformationDto;

import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardInformationListDto {

    private int userCardInformationId;

    private String cardNo;

    private String cardHolder;

    private int expirationMonth;

    private int expirationYear;

    private int cvv;

    private CustomerDto customerDto;
}
