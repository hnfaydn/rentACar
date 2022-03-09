package com.turkcell.rentACar.business.abstracts;


import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface AdditionalServiceService {

    DataResult<List<AdditionalServiceListDto>> getAll() throws BusinessException;

    Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException;

    DataResult<AdditionalServiceDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;
}
