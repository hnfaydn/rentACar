package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.entities.concretes.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandManager implements BrandService {

    private final BrandDao brandDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public BrandManager(BrandDao brandDao, ModelMapperService modelMapperService) {
        this.brandDao = brandDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<BrandListDto>> getAll() throws BusinessException {
        List<Brand> brands = brandDao.findAll();

        checkIfBrandListEmpty(brands);

        List<BrandListDto> brandListDtos = brands.stream()
                .map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(brandListDtos, "Data listed");
    }


    @Override
    public Result add(CreateBrandRequest createBrandRequest) throws BusinessException {

        Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);

        checkIfNameNotNull(brand.getName());

        checkIfNameNotDuplicated(brand.getName());

        this.brandDao.save(brand);
        return new SuccessDataResult(createBrandRequest, "Data added : " + brand.getName());
    }


    @Override
    public DataResult<BrandDto> getById(int id) throws BusinessException {

        checkIfIdExist(id);

        Brand brand = this.brandDao.getById(id);
        BrandDto brandDto = this.modelMapperService.forDto().map(brand, BrandDto.class);
        return new SuccessDataResult<>(brandDto, "Data getted by id");
    }


    @Override
    public Result update(int id, UpdateBrandRequest updateBrandRequest) throws BusinessException {

        checkIfIdExist(id);

        Brand brand = this.brandDao.getById(id);

        checkIfNameNotNull(updateBrandRequest.getName());

        checkIfNameNotDuplicated(updateBrandRequest.getName());

        String brandNameBeforeUpdate = this.brandDao.findById(id).getName();
        updateBrandOperations(brand, updateBrandRequest);
        this.brandDao.save(brand);

        return new SuccessResult(brandNameBeforeUpdate + " updated to " + updateBrandRequest.getName());
    }


    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExist(id);

        String brandNameBeforeDeleted = this.brandDao.getById(id).getName();
        this.brandDao.deleteById(id);
        return new SuccessResult("Data deleted : " + brandNameBeforeDeleted);
    }


    private void updateBrandOperations(Brand brand, UpdateBrandRequest updateBrandRequest) {
        brand.setName(updateBrandRequest.getName());
    }

    private void checkIfNameNotDuplicated(String name) throws BusinessException {
        if (this.brandDao.existsByName(name)) {
            throw new BusinessException("This brand is already exist in system: " + name);
        }
    }

    private void checkIfIdExist(int id) throws BusinessException {
        if (!this.brandDao.existsById(id)) {
            throw new BusinessException("There is no brand with following id : " + id);
        }
    }

    private void checkIfNameNotNull(String brandName) throws BusinessException {
        if (brandName.isEmpty() || brandName.isBlank()) {
            throw new BusinessException("Brand name can not empty or null!");
        }
    }

    private void checkIfBrandListEmpty(List<Brand> brands) throws BusinessException {
        if (brands.isEmpty()) {
            throw new BusinessException("There is no Brand to list");
        }
    }
}

