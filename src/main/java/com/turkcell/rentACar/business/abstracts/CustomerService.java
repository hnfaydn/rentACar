package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerListDto;
import com.turkcell.rentACar.business.requests.customerRequests.UpdateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.Customer;

import java.util.List;

public interface CustomerService {

    DataResult<List<CustomerListDto>> getAll();

    DataResult<CustomerDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateCustomerRequest updateCustomerRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    Customer getCustomerById(int id);


}
