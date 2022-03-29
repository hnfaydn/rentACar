package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.AdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.AdditionalService;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {

    private AdditionalServiceDao additionalServiceDao;
    private ModelMapperService modelMapperService;
    private RentalCarService rentalCarService;

    @Autowired
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao,
                                    ModelMapperService modelMapperService,
                                    RentalCarService rentalCarService
    ) {

        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() throws BusinessException {

        List<AdditionalService> additionalServices = this.additionalServiceDao.findAll();

        List<AdditionalServiceListDto> additionalServiceListDtos = additionalServices.stream()
                .map(additionalServiceListDto -> this.modelMapperService.forDto().map(additionalServiceListDto, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(additionalServiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);

        checkIfAdditionalServiceNameAlreadyExists(createAdditionalServiceRequest.getAdditionalServiceName());

        this.additionalServiceDao.save(additionalService);
        return new SuccessDataResult(createAdditionalServiceRequest, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<AdditionalServiceDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        AdditionalService additionalService = this.additionalServiceDao.getById(id);
        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService, AdditionalServiceDto.class);

        return new SuccessDataResult(additionalServiceDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {

        checkIfIdExists(id);

        AdditionalService additionalService = this.additionalServiceDao.getById(id);

        checkIfAdditionalServiceNameAlreadyExists(updateAdditionalServiceRequest.getAdditionalServiceName());
        additionalServiceUpdateOperations(additionalService, updateAdditionalServiceRequest);

        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService, AdditionalServiceDto.class);

        this.additionalServiceDao.save(additionalService);

        return new SuccessDataResult(additionalServiceDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);
        checkIfRentalCarHasDeletedAdditionalServiceId(id);

        AdditionalServiceDto additionalServiceDto =
                this.modelMapperService.forDto().map(this.additionalServiceDao.getById(id), AdditionalServiceDto.class);

        this.additionalServiceDao.deleteById(id);

        return new SuccessDataResult(additionalServiceDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public AdditionalService getAdditionalServiceById(int id) {

        if (this.additionalServiceDao.findAdditionalServiceByAdditionalServiceId(id) == null) {
            return null;
        }
        return this.additionalServiceDao.findAdditionalServiceByAdditionalServiceId(id);
    }


    private void checkIfAdditionalServiceNameAlreadyExists(String additionalServiceName) throws BusinessException {

        if (this.additionalServiceDao.existsByAdditionalServiceName(additionalServiceName)) {
            throw new BusinessException(BusinessMessages.AdditionalServiceMessages.ADDITIONAL_SERVICE_ALREADY_EXISTS + additionalServiceName);
        }
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.additionalServiceDao.existsById(id)) {
            throw new BusinessException(BusinessMessages.AdditionalServiceMessages.ADDITIONAL_SERVICE_NOT_FOUND + id);
        }
    }

    private void additionalServiceUpdateOperations(AdditionalService additionalService, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {

        additionalService.setAdditionalServiceName(updateAdditionalServiceRequest.getAdditionalServiceName());
        additionalService.setAdditionalServiceDailyPrice(updateAdditionalServiceRequest.getAdditionalServiceDailyPrice());
    }

    private void checkIfRentalCarHasDeletedAdditionalServiceId(int id) throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarService.getAllRentalCars();

        List<Integer> rentalCarIds = new ArrayList<>();
        for (RentalCar rentalCar : rentalCars
        ) {
            if (rentalCar.getAdditionalServices().contains(this.additionalServiceDao.getById(id))) {
                rentalCarIds.add(rentalCar.getRentalCarId());
            }
        }

        if (!rentalCarIds.isEmpty()) {
            throw new BusinessException(BusinessMessages.AdditionalServiceMessages.ADDITIONAL_SERVICE_ORDERED_BY_RENTAL_CARS + rentalCarIds);
        }
    }
}
