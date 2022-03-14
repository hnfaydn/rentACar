package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {

    private AdditionalServiceDao additionalServiceDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao,
                                    ModelMapperService modelMapperService) {

        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() throws BusinessException {

        List<AdditionalService> additionalServices = this.additionalServiceDao.findAll();

        List<AdditionalServiceListDto> additionalServiceListDtos = additionalServices.stream()
                .map(additionalServiceListDto -> this.modelMapperService.forDto().map(additionalServiceListDto, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(additionalServiceListDtos, "Data Listed Successfully:");
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);

        checkIfAdditionalServiceNameAlreadyExists(createAdditionalServiceRequest.getAdditionalServiceName());

        this.additionalServiceDao.save(additionalService);
        return new SuccessDataResult(createAdditionalServiceRequest, "Data Added Successfully:");
    }

    @Override
    public DataResult<AdditionalServiceDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        AdditionalService additionalService = this.additionalServiceDao.getById(id);
        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService, AdditionalServiceDto.class);

        return new SuccessDataResult(additionalServiceDto, "Data Brought Successfully:");
    }

    @Override
    public Result update(int id, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {

        checkIfIdExists(id);

        AdditionalService additionalService = this.additionalServiceDao.getById(id);

        checkIfAdditionalServiceNameAlreadyExists(updateAdditionalServiceRequest.getAdditionalServiceName());
        String additionalServiceNameBeforeUpdate = this.additionalServiceDao.getById(id).getAdditionalServiceName();
        additionalServiceUpdateOperations(additionalService, updateAdditionalServiceRequest);

        this.additionalServiceDao.save(additionalService);

        return new SuccessDataResult(additionalServiceNameBeforeUpdate + " Data updated, new data: " + additionalService.getAdditionalServiceName());
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);

        AdditionalServiceDto additionalServiceDto =
                this.modelMapperService.forDto().map(this.additionalServiceDao.getById(id), AdditionalServiceDto.class);

        this.additionalServiceDao.deleteById(id);

        return new SuccessDataResult(additionalServiceDto, "Data deleted successfully: ");
    }

    @Override
    public AdditionalService getAdditionalServiceById(int id) {

        return this.additionalServiceDao.getById(id);
    }


    private void checkIfAdditionalServiceNameAlreadyExists(String additionalServiceName) throws BusinessException {

        if (this.additionalServiceDao.existsByAdditionalServiceName(additionalServiceName)) {
            throw new BusinessException("Following additional service is already exists: " + additionalServiceName);
        }
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.additionalServiceDao.existsById(id)) {
            throw new BusinessException("There is no additional service with following id: " + id);
        }
    }

    private void additionalServiceUpdateOperations(AdditionalService additionalService, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {

        additionalService.setAdditionalServiceName(updateAdditionalServiceRequest.getAdditionalServiceName());
        additionalService.setAdditionalServiceDailyPrice(updateAdditionalServiceRequest.getAdditionalServiceDailyPrice());
    }
}
