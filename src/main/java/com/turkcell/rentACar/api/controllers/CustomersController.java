package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerListDto;
import com.turkcell.rentACar.business.requests.customerRequests.CreateCustomerRequest;
import com.turkcell.rentACar.business.requests.customerRequests.UpdateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customerController")
public class CustomersController {

    private CustomerService customerService;

    @Autowired
    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/getAll")
    DataResult<List<CustomerListDto>> getAll() throws BusinessException {
        return this.customerService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody CreateCustomerRequest createCustomerRequest) throws BusinessException{
        return this.customerService.add(createCustomerRequest);
    }

    @GetMapping("/getById")
    DataResult<CustomerDto> getById(@RequestParam int id) throws BusinessException{
        return this.customerService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id,@RequestBody UpdateCustomerRequest updateCustomerRequest) throws BusinessException{
        return this.customerService.update(id,updateCustomerRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException{
        return this.customerService.delete(id);
    }
}
