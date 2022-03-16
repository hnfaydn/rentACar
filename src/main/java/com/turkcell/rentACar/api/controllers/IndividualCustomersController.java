package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/individualCustomersController")
public class IndividualCustomersController {

    private IndividualCustomerService individualCustomerService;

    @Autowired
    public IndividualCustomersController(IndividualCustomerService individualCustomerService) {
        this.individualCustomerService = individualCustomerService;
    }
    @GetMapping("/getAll")
    DataResult<List<IndividualCustomerListDto>> getAll(){
        return this.individualCustomerService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody @Valid CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException {
        return this.individualCustomerService.add(createIndividualCustomerRequest);
    }
    @GetMapping("/getById")
    DataResult<IndividualCustomerDto> getById(@RequestParam int id) throws BusinessException {
        return this.individualCustomerService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id,@RequestBody @Valid UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException {
        return this.individualCustomerService.update(id,updateIndividualCustomerRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException {
        return this.individualCustomerService.delete(id);
    }
}
