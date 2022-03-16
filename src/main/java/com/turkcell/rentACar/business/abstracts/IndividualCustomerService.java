package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface IndividualCustomerService {

    DataResult<List<IndividualCustomerListDto>> getAll();

    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException;

    DataResult<IndividualCustomerDto> getById(int id) throws BusinessException;

    Result delete(int id) throws BusinessException;

    Result update(int id, UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException;
}
