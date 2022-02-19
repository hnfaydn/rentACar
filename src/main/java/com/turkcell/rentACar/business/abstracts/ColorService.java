package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.business.dtos.ColorDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.requests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface ColorService {

    DataResult<List<ColorListDto>> getAll();

    Result add(CreateColorRequest createColorRequest);

    DataResult<ColorDto> getById(int id);

    Result update(int id, UpdateColorRequest updateColorRequest);

    Result delete(int id);

}
