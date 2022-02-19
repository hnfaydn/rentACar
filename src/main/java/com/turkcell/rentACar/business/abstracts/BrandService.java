package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.business.dtos.BrandDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.requests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;


public interface BrandService {

    DataResult<List<BrandListDto>> getAll();

    Result add(CreateBrandRequest createBrandRequest);

    DataResult<BrandDto> getById(int id);

    Result update(int id, UpdateBrandRequest updateBrandRequest);

    Result delete(int id);


}
