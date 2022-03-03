package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.business.dtos.BrandDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.requests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;


public interface BrandService {

    DataResult<List<BrandListDto>> getAll() throws BusinessException;

    Result add(CreateBrandRequest createBrandRequest) throws BusinessException;

    DataResult<BrandDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateBrandRequest updateBrandRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;


}
