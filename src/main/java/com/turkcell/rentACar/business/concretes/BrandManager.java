package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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

        List<Brand> brands = this.brandDao.findAll();

        List<BrandListDto> brandListDtos = brands.stream()
                .map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(brandListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateBrandRequest createBrandRequest) throws BusinessException {

        Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);

        checkIfNameNotDuplicated(brand.getName());

        this.brandDao.save(brand);

        return new SuccessDataResult(createBrandRequest, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<BrandDto> getById(int id) throws BusinessException {

        checkIfBrandExists(id);

        Brand brand = this.brandDao.getById(id);
        BrandDto brandDto = this.modelMapperService.forDto().map(brand, BrandDto.class);

        return new SuccessDataResult(brandDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateBrandRequest updateBrandRequest) throws BusinessException {

        checkIfBrandExists(id);

        Brand brand = this.brandDao.getById(id);

        checkIfNameNotDuplicated(updateBrandRequest.getName());
        updateBrandOperations(brand, updateBrandRequest);

        BrandDto brandDto = this.modelMapperService.forDto().map(brand,BrandDto.class);

        this.brandDao.save(brand);

        return new SuccessDataResult(brandDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfBrandExists(id);

        BrandDto brandDto = this.modelMapperService.forDto().map(this.brandDao.getById(id),BrandDto.class);
        this.brandDao.deleteById(id);

        return new SuccessDataResult(brandDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }


    private void updateBrandOperations(Brand brand, UpdateBrandRequest updateBrandRequest) {

        brand.setName(updateBrandRequest.getName());
    }

    private void checkIfNameNotDuplicated(String name) throws BusinessException {

        if (this.brandDao.existsByName(name)) {
            throw new BusinessException( BusinessMessages.BrandMessages.BRAND_ALREADY_EXISTS + name);
        }
    }

    private void checkIfBrandExists(int id) throws BusinessException {

        if (!this.brandDao.existsById(id)) {
            throw new BusinessException(BusinessMessages.BrandMessages.BRAND_NOT_FOUND + id);
        }
    }

}

