package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.CarDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.requests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.entities.concretes.Car;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CarManager implements CarService {

    private CarDao carDao;
    private BrandDao brandDao;
    private ColorDao colorDao;
    private ModelMapperService modelMapperService;


    @Override
    public DataResult<List<CarListDto>>getAll() {

        List<Car> cars = carDao.findAll();

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(carListDtos,"Data listed");
    }


    @Override
    public Result add(CreateCarRequest createCarRequest){

           	Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
            if (!checkIfBrandIdExist(createCarRequest.getBrandId()).isSuccess()){

                return new ErrorResult("There is no brand with following id : " +createCarRequest.getBrandId());
            }

            if (!checkIfColorIdExist(createCarRequest.getColorId()).isSuccess()){

                return new ErrorResult("There is no color with following id : " +createCarRequest.getColorId());
            }

            if(!checkIfCarExist(car).isSuccess()) {

                return new ErrorDataResult(createCarRequest,"This car is already exists");

            }

            this.carDao.save(car);
            return new SuccessDataResult(createCarRequest,"Data added");

    }


    @Override
    public Result update(int id, UpdateCarRequest updateCarRequest){

            if(!checkIfIdExist(id).isSuccess()) {
                return new ErrorResult("There is no car with the following id to update : " + id);
            }

            Car car = this.carDao.getById(id);


            if(!carAndRequestParameterIsNotEqual(car,updateCarRequest).isSuccess()){
                return new ErrorResult("Initial values are completely equal to update values, no need to update!");
            }
            updateCarOperations(car, updateCarRequest);
            CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);
            this.carDao.save(car);

            return new SuccessDataResult(carDto,"Data updated, new data: ");
    }


    @Override
    public Result delete(int id){
        
            if(!checkIfIdExist(id).isSuccess()) {

                return new ErrorResult("There is no car with the following id to delete : " + id);
            }

            CarDto carDto = this.modelMapperService.forDto().map(this.carDao.getById(id), CarDto.class);
            this.carDao.deleteById(id);
            return new SuccessDataResult(carDto,"Data deleted");


    }
    @Override
	public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) {
		List<Car> cars = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);
		
		List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(carListDtos,"Data listed");
	}


	@Override
	public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<Car> cars = this.carDao.findAll(pageable).getContent();
		List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(carListDtos,"Data paged");
	}


	@Override
	public DataResult<List<CarListDto>> getAllSortedByDailyPrice(String sortType) {

            if (!checkIfSortTypeNotASCOrDESC(sortType).isSuccess()){
                return new ErrorDataResult("Please enter a correct sort type (ASC or DESC)");
            }

			Sort sort = Sort.by(Sort.Direction.fromString(sortType),"dailyPrice");
			List<Car> cars = this.carDao.findAll(sort);
			List<CarListDto> carListDtos = cars.stream()
	                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
	                .collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(carListDtos,"Data listed");
	}


    @Override
    public DataResult<CarDto> getById(int id){
        
            if(!checkIfIdExist(id).isSuccess()){
                return new ErrorDataResult(null,"There is no car with the following id: " + id);
            }

            Car car = this.carDao.getById(id);
            CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);
            return new SuccessDataResult<CarDto>(carDto,"Data getted");
    }

    private Result checkIfCarExist(Car car){
        if (
                carDao.existsByDailyPrice(car.getDailyPrice()) &&
                        carDao.existsByModelYear(car.getModelYear()) &&
                        carDao.existsByDescription(car.getDescription()) &&
                        carDao.existsByBrand_Id(car.getBrand().getId()) &&
                        carDao.existsByColor_Id(car.getColor().getId())
        ) {
             return new ErrorResult("This car is already exist!");
        }
        return new SuccessResult();
    }

    private void updateCarOperations(Car car, UpdateCarRequest updateCarRequest) {
        car.setDailyPrice(updateCarRequest.getDailyPrice());
        car.setDescription(updateCarRequest.getDescription());
    }

    private Result checkIfIdExist(int id){
        if (!this.carDao.existsById(id)) {
            return new ErrorResult("There is no car with this id: " + id);
        }
        return new SuccessResult();
    }

    private Result checkIfColorIdExist(int id){
        if(!this.colorDao.existsById(id)){
            return new ErrorResult("There is no color with following id: "+id);
        }
        return new SuccessResult();
    }

    private Result checkIfBrandIdExist(int id){
        if(!this.brandDao.existsById(id)){
            return new ErrorResult("There is no brand with following id: "+id);
        }
        return new SuccessResult();
    }

    private Result carAndRequestParameterIsNotEqual(Car car, UpdateCarRequest updateCarRequest){
        if(
                car.getDailyPrice()==updateCarRequest.getDailyPrice()&&
                        (car.getDescription()).equals(updateCarRequest.getDescription())

        ){
            return new ErrorResult("Initial values are completely equal to update values, no need to update");
        }
        return new SuccessResult();
    }

    private Result checkIfSortTypeNotASCOrDESC(String sortType){
        if(
            sortType.equals("ASC") || sortType.equals("DESC")
        ){
            return new SuccessResult();

        }
        return new ErrorResult("Please enter a correct sort type (ASC or DESC)");
    }
}
