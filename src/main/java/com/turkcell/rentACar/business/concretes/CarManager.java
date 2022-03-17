package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
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
import com.turkcell.rentACar.entities.concretes.AdditionalService;
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

        List<Car> cars = carDao.findAll();

        List<CarListDto> carListDtos = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data listed");
    }

    @Override
    public Result add(CreateCarRequest createCarRequest) throws BusinessException {

        List<CarDamage> tempCarDamageList = new ArrayList<>();

        for (Integer carDamageId : createCarRequest.getCarDamageIds()) {

            checkIfCarDamageIdExists(carDamageId);
            CarDamage carDamage = this.carDamageService.getCarDamageById(carDamageId);
            tempCarDamageList.add(carDamage);
        }


        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

        checkIfCarCreationParametersNotNull(car);
        checkIfCarExists(car);

        car.setCarId(0);
        car.setCarDamages(tempCarDamageList);

        this.carDao.save(car);

        return new SuccessDataResult(createCarRequest, "Data added");
    }

    private void checkIfCarDamageIdExists(Integer carDamageId) throws BusinessException {
        if(this.carDamageService.getCarDamageById(carDamageId)==null){
            throw new BusinessException("There is no car damage with following Id: "+carDamageId);
        }
    }

    @Override
    public Result update(int id, UpdateCarRequest updateCarRequest) throws BusinessException {

        checkIfIdExists(id);

        Car car = this.carDao.getById(id);

        checkIfCarAndUpdateParameterIsNotEqual(car, updateCarRequest);
        updateCarOperations(car, updateCarRequest);

        CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);

        this.carDao.save(car);

        return new SuccessDataResult(carDto, "Data updated, new data: ");
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);

        CarDto carDto = this.modelMapperService.forDto().map(this.carDao.getById(id), CarDto.class);

        this.carDao.deleteById(id);

        return new SuccessDataResult(carDto, "Data deleted");
    }

    @Override
    public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException {

        checkIfDailyPriceValid(dailyPrice);

        List<Car> cars = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);


        List<CarListDto> carListDtos =
                cars.stream().map(car -> this.modelMapperService.forDto()
                        .map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(carListDtos, "Data listed");
    }

    @Override
    public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {

        checkIfPageNoAndPageSizeValid(pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> cars = this.carDao.findAll(pageable).getContent();

        List<CarListDto> carListDtos =
                cars.stream().map(car -> this.modelMapperService.forDto()
                        .map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data paged");
    }

    @Override
    public DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) throws BusinessException {

        Sort sort = Sort.by(sortDirection, "dailyPrice");
        List<Car> cars = this.carDao.findAll(sort);

        List<CarListDto> carListDtos = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(carListDtos, "Data listed");
    }

    @Override
    public void carKilometerSetOperation(int carId, double kilometer) throws BusinessException {

        Car car = this.carDao.getById(carId);

        car.setKilometerInformation(kilometer);

        this.carDao.save(car);
    }

    @Override
    public DataResult<CarDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        Car car = this.carDao.getById(id);
        CarDto carDto = this.modelMapperService.forDto().map(car, CarDto.class);

        return new SuccessDataResult<>(carDto, "Data getted");
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
            throw new BusinessException("This car is already exist!");
        }
    }

    private void updateCarOperations(Car car, UpdateCarRequest updateCarRequest) {

        car.setDailyPrice(updateCarRequest.getDailyPrice());
        car.setDescription(updateCarRequest.getDescription());
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.carDao.existsById(id)) {
            throw new BusinessException("There is no car with following id: " + id);
        }
    }

    private void checkIfCarAndUpdateParameterIsNotEqual(Car car, UpdateCarRequest updateCarRequest) throws BusinessException {

        if (car.getDailyPrice() == updateCarRequest.getDailyPrice() && car.getDescription().equals(updateCarRequest.getDescription())

        ) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
        }
    }

    private void checkIfCarCreationParametersNotNull(Car car) throws BusinessException {

        if (this.brandService.getById(car.getBrand().getBrandId()) == null) {
            throw new BusinessException("There is no brand with following id: " + car.getBrand().getBrandId());
        }

        if (this.colorService.getById(car.getColor().getColorId()) == null) {
            throw new BusinessException("There is no color with following id: " + car.getColor().getColorId());
        }
    }

    private void checkIfPageNoAndPageSizeValid(int pageNo, int pageSize) throws BusinessException {

        if (pageNo <= 0) {
            throw new BusinessException("Page No can not less than or equal to zero");
        }

        if (pageSize <= 0) {
            throw new BusinessException("Page Size can not less than or equal to zero");
        }

    }

    private void checkIfDailyPriceValid(double dailyPrice) throws BusinessException {

        if (dailyPrice <= 0) {
            throw new BusinessException("Daily price can not less than or equal to zero");
        }
    }
}
