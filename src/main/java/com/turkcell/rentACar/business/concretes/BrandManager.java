package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.BrandDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.requests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.entities.concretes.Brand;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {

    private BrandDao brandDao;
    private ModelMapperService modelMapperService;


    @Override
    public DataResult<List<BrandListDto>> getAll() {
        List<Brand> brands = brandDao.findAll();

		if(!checkIfBrandListEmpty(brands).isSuccess()){
			return new ErrorDataResult(checkIfBrandListEmpty(brands).getMessage());
		}

        List<BrandListDto> brandListDtos = brands.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
				.collect(Collectors.toList());

        return new SuccessDataResult<>(brandListDtos, "Data listed");
    }


    @Override
    public Result add(CreateBrandRequest createBrandRequest){

	    Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);

		if(!checkIfNameNotNull(brand.getName()).isSuccess()){
			return new ErrorResult(checkIfNameNotNull(brand.getName()).getMessage());
		}

		if(!checkIfNameNotDuplicated(brand.getName()).isSuccess()) {
				return new ErrorDataResult(createBrandRequest,checkIfNameNotDuplicated(brand.getName()).getMessage());
		}

		this.brandDao.save(brand);
		return new SuccessDataResult(createBrandRequest,"Data added : " + brand.getName());
    }


    @Override
    public DataResult<BrandDto> getById(int id){

		if(!checkIfIdExist(id).isSuccess()) {
			return new ErrorDataResult(checkIfIdExist(id).getMessage());
		}

		Brand brand = this.brandDao.getById(id);
		BrandDto brandDto = this.modelMapperService.forDto().map(brand, BrandDto.class);
		return new SuccessDataResult<>(brandDto,"Data getted by id");
    }


    @Override
    public Result update(int id, UpdateBrandRequest updateBrandRequest){

		if(!checkIfIdExist(id).isSuccess()) {
			return new ErrorResult(checkIfIdExist(id).getMessage());
		}

		Brand brand = this.brandDao.getById(id);

		if(!checkIfNameNotNull(updateBrandRequest.getName()).isSuccess()){
			return new ErrorResult(checkIfNameNotNull(updateBrandRequest.getName()).getMessage());
		}

		if(!checkIfNameNotDuplicated(updateBrandRequest.getName()).isSuccess()) {
			return new ErrorResult(checkIfNameNotDuplicated(updateBrandRequest.getName()).getMessage());
		}
		String brandNameBeforeUpdate = this.brandDao.findById(id).getName();
		updateBrandOperations(brand,updateBrandRequest);
		this.brandDao.save(brand);

		return new SuccessResult(brandNameBeforeUpdate+" updated to "+updateBrandRequest.getName());
    }


    @Override
    public Result delete(int id){
		
		if(!checkIfIdExist(id).isSuccess()) {
			return new ErrorResult(checkIfIdExist(id).getMessage());
		}
		String brandNameBeforeDeleted = this.brandDao.getById(id).getName();
		this.brandDao.deleteById(id);
		return new SuccessResult("Data deleted : " + brandNameBeforeDeleted );
    }


	private void updateBrandOperations(Brand brand, UpdateBrandRequest updateBrandRequest){
		brand.setName(updateBrandRequest.getName());
	}

    private Result checkIfNameNotDuplicated(String name){
        if (this.brandDao.existsByName(name)) {
        	return new ErrorResult("This brand is already exist in system: "+name);
        }        
        return new SuccessResult();
    }

	private Result checkIfIdExist(int id){
		if (!this.brandDao.existsById(id)) {
			return new ErrorResult("There is no brand with following id : " + id);
		}
		return new SuccessResult();
	}

	private Result checkIfNameNotNull(String brandName){
		if(brandName.isEmpty() || brandName.isBlank()){
			return new ErrorResult("Brand name can not empty or null!");
		}
		return new SuccessResult();
	}

	private Result checkIfBrandListEmpty(List<Brand> brands){
		if(brands.isEmpty()){
			return new ErrorDataResult("There is no Brand to list");
		}
		return new SuccessResult();
	}
}

