package com.turkcell.rentACar.api.models;

import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedRentalCarAndInvoiceDto {

    private RentalCarDto updatedRentalCar;

    private InvoiceDto updatedInvoice;
}
