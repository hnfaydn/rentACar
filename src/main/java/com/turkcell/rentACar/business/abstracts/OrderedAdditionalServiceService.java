package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;

import java.util.List;

public interface OrderedAdditionalServiceService {

    DataResult<List<OrderedAdditionalServiceListDto>> getAll() throws BusinessException;

    Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException;

    DataResult<OrderedAdditionalServiceDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    OrderedAdditionalService updateRentalCarOrderedAdditionalService(int id, List<Integer> additionalServices) throws BusinessException;

    List<OrderedAdditionalService> getAllOrderedAdditionalServices();

}
