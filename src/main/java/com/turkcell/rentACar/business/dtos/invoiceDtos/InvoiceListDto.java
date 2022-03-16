package com.turkcell.rentACar.business.dtos.invoiceDtos;

import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListDto {

    private Integer invoiceId;

    private Integer invoiceNumber;

    private LocalDate invoiceDate;

    private Double additionalServiceTotalPayment;

    private Integer rentDay;

    private Double rentPayment;

    private Double rentLocationPayment;

    private Double totalPayment;

    private RentalCarListDto rentalCarListDto;
}
