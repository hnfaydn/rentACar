package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerListDto;
import com.turkcell.rentACar.business.requests.customerRequests.UpdateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CustomerService {

    DataResult<List<CustomerListDto>> getAll();

    DataResult<CustomerDto> getById(int id);

    Result update(int id, UpdateCustomerRequest updateCustomerRequest);

    Result delete(int id) throws BusinessException;

}
