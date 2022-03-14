package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface RentalCarService {

    DataResult<List<RentalCarListDto>> getAll() throws BusinessException;

    Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException;

    DataResult<RentalCarDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    List<RentalCarListDto> getAllRentalCarsByCarId(int carId) throws BusinessException;

    void rentalCarOrderedAdditionalServiceIdSetOperation(int orderedAdditionalServiceId);
}
