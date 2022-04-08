package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import com.turkcell.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.turkcell.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
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

        List<CarDamageListDto> carDamageListDtos =
                this.carDamageDao.findAll().stream()
                .map(carDamage -> this.modelMapperService.forDto().map(carDamage, CarDamageListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(carDamageListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateCarDamageRequest createCarDamageRequest) throws BusinessException {

        checkIfCarDamageIsAlreadyExists(createCarDamageRequest.getDamageDescription());

        CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest, CarDamage.class);

        carDamage.setCarDamageId(0);
        this.carDamageDao.save(carDamage);

        return new SuccessDataResult(createCarDamageRequest, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<CarDamageDto> getById(int id) throws BusinessException {

        checkIfCarDamageIdExists(id);

        CarDamageDto carDamageDto = this.modelMapperService.forDto()
                .map(this.carDamageDao.getById(id), CarDamageDto.class);

        return new SuccessDataResult(carDamageDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateCarDamageRequest updateCarDamageRequest) throws BusinessException {

        checkIfCarDamageIdExists(id);
        checkIfCarDamageIsAlreadyExists(updateCarDamageRequest.getDamageDescription());

        CarDamage carDamage = this.carDamageDao.getById(id);

        carDamageUpdateOperations(carDamage, updateCarDamageRequest);

        CarDamageDto carDamageDto = this.modelMapperService.forDto().map(carDamage, CarDamageDto.class);

        this.carDamageDao.save(carDamage);

        return new SuccessDataResult(carDamageDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfCarDamageIdExists(id);

        CarDamageDto carDamageDto = this.modelMapperService.forDto()
                .map(this.carDamageDao.getById(id), CarDamageDto.class);

        this.carDamageDao.deleteById(id);

        return new SuccessDataResult(carDamageDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<CarDamage> getCarDamageById(int id) {

        if (!this.carDamageDao.existsById(id)) {
            return new ErrorDataResult(null);
        }

        return new SuccessDataResult(this.carDamageDao.getById(id));
    }

    private void checkIfCarDamageIsAlreadyExists(String damageDescription) throws BusinessException {

        if (this.carDamageDao.existsByDamageDescription(damageDescription)) {
            throw new BusinessException(BusinessMessages.CarDamageMessages.CAR_DAMAGE_ALREADY_EXISTS + damageDescription);
        }
    }

    private void checkIfCarDamageIdExists(int id) throws BusinessException {

        if (!this.carDamageDao.existsById(id)) {
            throw new BusinessException(BusinessMessages.CarDamageMessages.CAR_DAMAGE_NOT_FOUND + id);
        }
    }

    private void carDamageUpdateOperations(CarDamage carDamage, UpdateCarDamageRequest updateCarDamageRequest) {

        carDamage.setDamageDescription(updateCarDamageRequest.getDamageDescription());
    }
}
