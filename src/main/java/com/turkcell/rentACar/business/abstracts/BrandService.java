package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.brandDtos.BrandDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;


public interface BrandService {

    DataResult<List<BrandListDto>> getAll() throws BusinessException;

    Result add(CreateBrandRequest createBrandRequest) throws BusinessException;

    DataResult<BrandDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateBrandRequest updateBrandRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;


}
