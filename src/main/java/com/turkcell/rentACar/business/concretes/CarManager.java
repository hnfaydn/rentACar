package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
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
    public DataResult<List<CarListDto>> getAll() throws BusinessException {

        List<Car> cars = carDao.findAll();

        checkIfCarListEmpty(cars);


        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data listed");
    }


    @Override
    public Result add(CreateCarRequest createCarRequest) throws BusinessException {

        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

        checkIfCarCreationParametersNotNull(car);

        checkIfCarExist(car);

        this.carDao.save(car);
        return new SuccessDataResult(createCarRequest, "Data added");
    }


    @Override
    public Result update(int id, UpdateCarRequest updateCarRequest) throws BusinessException {

        checkIfIdExist(id);

        Car car = this.carDao.getById(id);

        checkIfCarUpdateParametersNotNull(updateCarRequest);

        carAndRequestParameterIsNotEqual(car, updateCarRequest);

        updateCarOperations(car, updateCarRequest);

        CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);
        this.carDao.save(car);

        return new SuccessDataResult(carDto, "Data updated, new data: ");
    }


    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExist(id);

        CarDto carDto = this.modelMapperService.forDto().map(this.carDao.getById(id), CarDto.class);
        this.carDao.deleteById(id);
        return new SuccessDataResult(carDto, "Data deleted");
    }


    @Override
    public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException {

        checkIfDailyPriceValid(dailyPrice);

        List<Car> cars = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

        checkIfCarListEmpty(cars);

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(carListDtos, "Data listed");
    }


    @Override
    public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {

        checkIfPageNoAndPageSizeValid(pageNo,pageSize);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> cars = this.carDao.findAll(pageable).getContent();

        checkIfCarListEmpty(cars);

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data paged");
    }


    @Override
    public DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) throws BusinessException {

        Sort sort = Sort.by(sortDirection, "dailyPrice");
        List<Car> cars = this.carDao.findAll(sort);

        checkIfCarListEmpty(cars);

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data listed");
    }


    @Override
    public DataResult<CarDto> getById(int id) throws BusinessException {

        checkIfIdExist(id);

        Car car = this.carDao.getById(id);
        CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);
        return new SuccessDataResult<>(carDto, "Data getted");
    }


    private void checkIfCarExist(Car car) throws BusinessException {
        if (
                carDao.existsByDailyPrice(car.getDailyPrice())   &&
                carDao.existsByModelYear(car.getModelYear())     &&
                carDao.existsByDescription(car.getDescription()) &&
                carDao.existsByBrand_BrandId(car.getBrand().getBrandId())  &&
                carDao.existsByColor_ColorId(car.getColor().getColorId())
        ) {
            throw new BusinessException("This car is already exist!");
        }

    }

    private void updateCarOperations(Car car, UpdateCarRequest updateCarRequest) {
        car.setDailyPrice(updateCarRequest.getDailyPrice());
        car.setDescription(updateCarRequest.getDescription());
    }

    private void checkIfIdExist(int id) throws BusinessException {
        if (!this.carDao.existsById(id)) {
            throw new BusinessException("There is no car with following id: " + id);
        }

    }

    private void carAndRequestParameterIsNotEqual(Car car, UpdateCarRequest updateCarRequest) throws BusinessException {
        if (
             car.getDailyPrice() == updateCarRequest.getDailyPrice() &&
             car.getDescription().equals(updateCarRequest.getDescription())

        ) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
        }

    }

    private void checkIfCarCreationParametersNotNull(Car car) throws BusinessException {
        Double dailyPrice = car.getDailyPrice();
        if (dailyPrice <= 0 || dailyPrice == null || dailyPrice.isNaN()) {
            throw new BusinessException("Daily price can not less than zero or equal to zero or null!");
        }

        Integer modelYear = car.getModelYear();
        if (modelYear <= 0 || modelYear == null) {
            throw new BusinessException("Model year can not less than zero or equal to zero or null!");
        }

        if (car.getDescription().isEmpty() || car.getDescription().isBlank()) {
            throw new BusinessException("Car description can not null or empty!");
        }

        if (!this.brandDao.existsById(car.getBrand().getBrandId())) {
            throw new BusinessException("There is no brand with following id: " + car.getBrand().getBrandId());
        }

        if (!this.colorDao.existsById(car.getColor().getColorId())) {
            throw new BusinessException("There is no color with following id: " + car.getColor().getColorId());
        }


    }

    private void checkIfCarUpdateParametersNotNull(UpdateCarRequest updateCarRequest) throws BusinessException {
        Double dailyPrice = updateCarRequest.getDailyPrice();
        if (dailyPrice <= 0 || dailyPrice == null || dailyPrice.isNaN()) {
            throw new BusinessException("Daily price can not less than zero or equal to zero or null!");
        }

        if (updateCarRequest.getDescription().isEmpty() || updateCarRequest.getDescription().isBlank()) {
            throw new BusinessException("Car description can not null or empty!");
        }

    }

    private void checkIfCarListEmpty(List<Car> cars) throws BusinessException {
        if(cars.isEmpty()){
            throw new BusinessException("There is no Car to list");
        }
    }

    private void checkIfPageNoAndPageSizeValid(int pageNo, int pageSize) throws BusinessException {
        if(pageNo<=0){
            throw new BusinessException("Page No can not less than or equal to zero");
        }

        if(pageSize<=0){
            throw new BusinessException("Page Size can not less than or equal to zero");
        }

    }

    private void checkIfDailyPriceValid(double dailyPrice) throws BusinessException {
        if (dailyPrice<=0){
            throw new BusinessException("Daily price can not less than or equal to zero");
        }

    }
}
