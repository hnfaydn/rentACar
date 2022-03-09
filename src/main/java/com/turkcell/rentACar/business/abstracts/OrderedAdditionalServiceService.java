package com.turkcell.rentACar.business.abstracts;


import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface OrderedAdditionalServiceService {

    DataResult<List<OrderedAdditionalServiceListDto>> getAll() throws BusinessException;

    Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalRequest)  throws BusinessException;

    DataResult<OrderedAdditionalServiceDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;
}

