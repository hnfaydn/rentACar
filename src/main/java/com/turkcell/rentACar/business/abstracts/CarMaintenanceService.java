package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CarMaintenanceService {

    DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException;

    Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException;

    DataResult<CarMaintenanceDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    List<CarMaintenanceListDto> getAllCarMaintenancesByCarId(int carId) throws BusinessException;


}
