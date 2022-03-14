package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityListDto;
import com.turkcell.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.turkcell.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CityService {

    DataResult<List<CityListDto>> getAll() throws BusinessException;

    Result add(CreateCityRequest createCityRequest) throws BusinessException;

    DataResult<CityDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateCityRequest updateCityRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;
}
