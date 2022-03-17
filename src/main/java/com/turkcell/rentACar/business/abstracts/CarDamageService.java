package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import com.turkcell.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.turkcell.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.CarDamage;

import java.util.List;

public interface CarDamageService {

    DataResult<List<CarDamageListDto>> getAll() throws BusinessException;

    Result add(CreateCarDamageRequest createCarDamageRequest) throws BusinessException;

    DataResult<CarDamageDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateCarDamageRequest updateCarDamageRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    CarDamage getCarDamageById(int id) throws BusinessException;
}
