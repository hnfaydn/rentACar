package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.colorDtos.ColorDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface ColorService {

    DataResult<List<ColorListDto>> getAll() throws BusinessException;

    Result add(CreateColorRequest createColorRequest) throws BusinessException;

    DataResult<ColorDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateColorRequest updateColorRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

}
