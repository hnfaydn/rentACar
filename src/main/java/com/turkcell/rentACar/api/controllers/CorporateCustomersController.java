package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/corporateCustomersController")
public class CorporateCustomersController {

    private CorporateCustomerService corporateCustomerService;

    @Autowired
    public CorporateCustomersController(CorporateCustomerService corporateCustomerService) {
        this.corporateCustomerService = corporateCustomerService;
    }
    @GetMapping("/getAll")
    DataResult<List<CorporateCustomerListDto>> getAll(){
        return this.corporateCustomerService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody @Valid CreateCorporateCustomerRequest createIndividualCustomerRequest) throws BusinessException {
        return this.corporateCustomerService.add(createIndividualCustomerRequest);
    }
    @GetMapping("/getById")
    DataResult<CorporateCustomerDto> getById(@RequestParam int id) throws BusinessException {
        return this.corporateCustomerService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id,@RequestBody @Valid UpdateCorporateCustomerRequest updateIndividualCustomerRequest) throws BusinessException {
        return this.corporateCustomerService.update(id,updateIndividualCustomerRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException {
        return this.corporateCustomerService.delete(id);
    }
}
