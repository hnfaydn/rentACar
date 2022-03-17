package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarListDto;
import com.turkcell.rentACar.business.requests.carRequests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CarService {

    DataResult<List<CarListDto>> getAll() throws BusinessException;

    Result add(CreateCarRequest createCarRequest) throws BusinessException;

    Result update(int id, UpdateCarRequest updateCarRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

    DataResult<CarDto> getById(int id) throws BusinessException;

    DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException;

    DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException;

    DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) throws BusinessException;

    void carKilometerSetOperation(int carId, double kilometer) throws BusinessException;

}
