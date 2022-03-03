package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.business.dtos.ColorDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.requests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface ColorService {

    DataResult<List<ColorListDto>> getAll() throws BusinessException;

    Result add(CreateColorRequest createColorRequest) throws BusinessException;

    DataResult<ColorDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateColorRequest updateColorRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;

}
