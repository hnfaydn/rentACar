package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityListDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.turkcell.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/invoicesController")
public class InvoicesController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/getAll")
    DataResult<List<InvoiceListDto>> getAll() throws BusinessException{
        return this.invoiceService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody @Valid CreateInvoiceRequest createInvoiceRequest) throws BusinessException{
        return this.invoiceService.add(createInvoiceRequest);
    }

    @GetMapping("/getById")
    DataResult<InvoiceDto> getById(@RequestParam int id) throws BusinessException{
        return this.invoiceService.getById(id);
    }

    @PutMapping("/reGenerateInvoiceByUsingId")
    Result update(@RequestParam int id) throws BusinessException{
        return this.invoiceService.reGenerateInvoiceByUsingRentalCarId(id);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException{
        return this.invoiceService.delete(id);
    }


}
