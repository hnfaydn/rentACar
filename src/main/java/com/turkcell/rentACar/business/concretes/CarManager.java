package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarListDto;
import com.turkcell.rentACar.business.requests.carRequests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.entities.concretes.Car;
import com.turkcell.rentACar.entities.concretes.CarDamage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarManager implements CarService {

    private final CarDao carDao;
    private final BrandService brandService;
    private final ColorService colorService;
    private final ModelMapperService modelMapperService;
    private CarDamageService carDamageService;


    @Autowired
    public CarManager(CarDao carDao,
                      BrandService brandService,
                      ColorService colorService,
                      ModelMapperService modelMapperService,
                      CarDamageService carDamageService) {
        this.carDao = carDao;
        this.brandService = brandService;
        this.colorService = colorService;
        this.modelMapperService = modelMapperService;
        this.carDamageService = carDamageService;
    }

    @Override
    public DataResult<List<CarListDto>> getAll() throws BusinessException {

        List<CarListDto> carListDtos =
                this.carDao.findAll().stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateCarRequest createCarRequest) throws BusinessException {

        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

        checkIfCarCreationParametersNotNull(car);
        checkIfCarExists(car);
        checkIfCarDamageListIsNullOrEmpty(createCarRequest.getCarDamageIds(),car);

        car.setCarId(0);

        this.carDao.save(car);

        return new SuccessDataResult(createCarRequest, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateCarRequest updateCarRequest) throws BusinessException {

        checkIfIdExists(id);

        Car car = this.carDao.getById(id);

        checkIfCarAndUpdateParameterIsNotEqual(car, updateCarRequest);
        updateCarOperations(car, updateCarRequest);

        CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);

        this.carDao.save(car);

        return new SuccessDataResult(carDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);

        CarDto carDto = this.modelMapperService.forDto()
                .map(this.carDao.getById(id), CarDto.class);

        this.carDao.deleteById(id);

        return new SuccessDataResult(carDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException {

        checkIfDailyPriceValid(dailyPrice);

        List<Car> cars = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto()
                        .map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(carListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {

        checkIfPageNoAndPageSizeValid(pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Car> cars = this.carDao.findAll(pageable).getContent();

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) {

        Sort sort = Sort.by(sortDirection, "dailyPrice");

        List<Car> cars = this.carDao.findAll(sort);

        List<CarListDto> carListDtos = cars.stream()
                .map(car -> this.modelMapperService.forDto().map(car, CarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public void carKilometerSetOperation(int carId, double kilometer){

        Car car = this.carDao.getById(carId);

        car.setKilometerInformation(kilometer);

        this.carDao.save(car);
    }

    @Override
    public DataResult<CarDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        CarDto carDto = this.modelMapperService.forDto()
                .map(this.carDao.getById(id), CarDto.class);

        return new SuccessDataResult<>(carDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    private void checkIfCarExists(Car car) throws BusinessException {

        if (this.carDao.existsCarByBrand_BrandIdAndColor_ColorIdAndDailyPriceAndModelYearAndDescription(
                car.getBrand().getBrandId(),
                car.getColor().getColorId(),
                car.getDailyPrice(),
                car.getModelYear(),
                car.getDescription()
                ))
        {
            throw new BusinessException(BusinessMessages.CarMessages.CAR_ALREADY_EXISTS);
        }
    }

    private void updateCarOperations(Car car, UpdateCarRequest updateCarRequest) throws BusinessException {

        car.setDailyPrice(updateCarRequest.getDailyPrice());
        car.setDescription(updateCarRequest.getDescription());
        checkIfCarDamageListIsNullOrEmpty(updateCarRequest.getCarDamageIds(),car);
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.carDao.existsById(id)) {
            throw new BusinessException(BusinessMessages.CarMessages.CAR_NOT_FOUND + id);
        }
    }

    private void checkIfCarAndUpdateParameterIsNotEqual(Car car, UpdateCarRequest updateCarRequest) throws BusinessException {

        List<CarDamage> tempCarDamageList = new ArrayList<>();

        for (Integer carDamageId : updateCarRequest.getCarDamageIds()) {

            checkIfCarDamageIdExists(carDamageId);
            CarDamage carDamage = this.carDamageService.getCarDamageById(carDamageId).getData();
            tempCarDamageList.add(carDamage);
        }

        if (car.getDailyPrice() == updateCarRequest.getDailyPrice() &&
                car.getDescription().equals(updateCarRequest.getDescription()) &&
                car.getCarDamages() == tempCarDamageList
        ) {
            throw new BusinessException(BusinessMessages.CarMessages.NO_CHANGES_NO_NEED_TO_UPDATE);
        }
    }

    private void checkIfCarCreationParametersNotNull(Car car) throws BusinessException {

        if (this.brandService.getById(car.getBrand().getBrandId()) == null) {
            throw new BusinessException(BusinessMessages.CarMessages.BRAND_NOT_FOUND + car.getBrand().getBrandId());
        }

        if (this.colorService.getById(car.getColor().getColorId()) == null) {
            throw new BusinessException(BusinessMessages.CarMessages.COLOR_NOT_FOUND + car.getColor().getColorId());
        }
    }

    private void checkIfPageNoAndPageSizeValid(int pageNo, int pageSize) throws BusinessException {

        if (pageNo <= 0) {
            throw new BusinessException(BusinessMessages.CarMessages.PAGE_NO_CANNOT_LESS_THAN_ZERO);
        }

        if (pageSize <= 0) {
            throw new BusinessException(BusinessMessages.CarMessages.PAGE_SIZE_CANNOT_LESS_THAN_ZERO);
        }

    }

    private void checkIfDailyPriceValid(double dailyPrice) throws BusinessException {

        if (dailyPrice < 0) {
            throw new BusinessException(BusinessMessages.CarMessages.DAILY_PRICE_CANNOT_LESS_THAN_ZERO);
        }
    }

    private void checkIfCarDamageListIsNullOrEmpty(List<Integer> carDamageIds, Car car) throws BusinessException {

        if(carDamageIds == null || carDamageIds.isEmpty())
        {
            car.setCarDamages(null);
        }else{
            List<CarDamage> tempCarDamageList = new ArrayList<>();

            for (Integer carDamageId : carDamageIds) {

                checkIfCarDamageIdExists(carDamageId);
                CarDamage carDamage = this.carDamageService.getCarDamageById(carDamageId).getData();
                tempCarDamageList.add(carDamage);
            }
            car.setCarDamages(tempCarDamageList);
        }
    }

    private void checkIfCarDamageIdExists(Integer carDamageId) throws BusinessException {

        if(this.carDamageService.getById(carDamageId).getData() == null||carDamageId<=0){
            throw new BusinessException(BusinessMessages.CarMessages.CAR_DAMAGE_NOT_FOUND+carDamageId);
        }
    }
}
