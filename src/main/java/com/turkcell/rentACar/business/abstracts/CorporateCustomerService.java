package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CorporateCustomerService {

    DataResult<List<CorporateCustomerListDto>> getAll();

    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException;

    DataResult<CorporateCustomerDto> getById(int id) throws BusinessException;

    Result delete(int id) throws BusinessException;

    Result update(int id, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException;

}
