package com.turkcell.rentACar.business.dtos.paymentDtos;

import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentListDto {

    private int paymentInformationId;

    private String cardNo;

    private double paymentAmount;

    private RentalCarDto rentalCarDto;
}
