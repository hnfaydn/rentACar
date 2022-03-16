package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface InvoiceService {

    DataResult<List<InvoiceListDto>> getAll() throws BusinessException;

    Result add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException;

    DataResult<InvoiceDto> getById(int id) throws BusinessException;

    Result reGenerateInvoiceByUsingRentalCarId(int id) throws BusinessException;

    Result delete(int id) throws BusinessException;

    DataResult<List<InvoiceListDto>> getAllInvoicesByRentalCarId(int id) throws BusinessException;
}
