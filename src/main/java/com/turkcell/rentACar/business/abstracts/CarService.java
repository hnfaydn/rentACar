package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.business.dtos.CarDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.requests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarService {

    DataResult<List<CarListDto>> getAll();

    Result add(CreateCarRequest createCarRequest);

    Result update(int id, UpdateCarRequest updateCarRequest);

    Result delete(int id);

    DataResult<CarDto> getById(int id);
    
    DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice);
    
    DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize);
    
    DataResult<List<CarListDto>> getAllSortedByDailyPrice(String sortType);
}
