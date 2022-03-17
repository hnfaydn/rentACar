package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.turkcell.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDamageDao;
import com.turkcell.rentACar.entities.concretes.CarDamage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarDamageManager implements CarDamageService {

    private CarDamageDao carDamageDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CarDamageManager(CarDamageDao carDamageDao,
                            ModelMapperService modelMapperService) {
        this.carDamageDao = carDamageDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<CarDamageListDto>> getAll() throws BusinessException {
        List<CarDamage> carDamages = this.carDamageDao.findAll();

        List<CarDamageListDto> carDamageListDtos = carDamages.stream()
                .map(carDamage -> this.modelMapperService.forDto().map(carDamage, CarDamageListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(carDamageListDtos,"Data Listed Successfully");
    }

    @Override
    public Result add(CreateCarDamageRequest createCarDamageRequest) throws BusinessException {

        CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest,CarDamage.class);

        checkIfCarDamageIsAlreadyExists(carDamage.getDamageDescription());

        carDamage.setCarDamageId(0);
        this.carDamageDao.save(carDamage);

        return new SuccessDataResult(createCarDamageRequest,"Data Added Successfully");
    }



    @Override
    public DataResult<CarDamageDto> getById(int id) throws BusinessException {

        chechIfCarDamageIdExists(id);

        CarDamage carDamage = this.carDamageDao.getById(id);

        CarDamageDto carDamageDto = this.modelMapperService.forDto().map(carDamage,CarDamageDto.class);

        return new SuccessDataResult(carDamageDto,"Data brought successfully");
    }



    @Override
    public Result update(int id, UpdateCarDamageRequest updateCarDamageRequest) throws BusinessException {

        chechIfCarDamageIdExists(id);
        checkIfCarDamageIsAlreadyExists(updateCarDamageRequest.getDamageDescription());

        CarDamage carDamage = this.carDamageDao.getById(id);

        carDamageUpdateOperations(carDamage,updateCarDamageRequest);

        CarDamageDto carDamageDto = this.modelMapperService.forDto().map(carDamage,CarDamageDto.class);

        this.carDamageDao.save(carDamage);

        return new SuccessDataResult(carDamageDto,"Data updated to following data");
    }



    @Override
    public Result delete(int id) throws BusinessException {

        chechIfCarDamageIdExists(id);

        CarDamage carDamage = this.carDamageDao.getById(id);
        CarDamageDto carDamageDto = this.modelMapperService.forDto().map(carDamage,CarDamageDto.class);

        this.carDamageDao.deleteById(id);

        return new SuccessDataResult(carDamageDto,"Data deleted successfuly");
    }

    @Override
    public CarDamage getCarDamageById(int id) throws BusinessException {
        return this.carDamageDao.getById(id);
    }

    private void checkIfCarDamageIsAlreadyExists(String damageDescription) throws BusinessException {
        if(this.carDamageDao.existsByDamageDescription(damageDescription)){
            throw new BusinessException("This car damage description is already exists: "+damageDescription);
        }
    }

    private void chechIfCarDamageIdExists(int id) throws BusinessException {
        if(!this.carDamageDao.existsById(id)){
            throw new BusinessException("There is no car damage with following id: " +id);
        }
    }
    private void carDamageUpdateOperations(CarDamage carDamage, UpdateCarDamageRequest updateCarDamageRequest) {
        carDamage.setDamageDescription(updateCarDamageRequest.getDamageDescription());
    }
}
