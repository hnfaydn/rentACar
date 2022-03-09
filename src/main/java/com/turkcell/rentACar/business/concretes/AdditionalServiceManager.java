package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceListDto;
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
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao, ModelMapperService modelMapperService) {
        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() throws BusinessException {

        List<AdditionalService> additionalServices = additionalServiceDao.findAll();

        List<AdditionalServiceListDto> additionalServiceListDtos = additionalServices.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(additionalServiceListDtos, "Data listed");
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);

        additionalService.setAdditionalServiceId(0);
        additionalService.setName(createAdditionalServiceRequest.getName());
        additionalService.setDailyPrice(createAdditionalServiceRequest.getDailyPrice());

        this.additionalServiceDao.save(additionalService);

        return new SuccessDataResult(createAdditionalServiceRequest, "Data added");
    }

    @Override
    public DataResult<AdditionalServiceDto> getById(int id) throws BusinessException {

        AdditionalService additionalService = this.additionalServiceDao.getById(id);

        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService, AdditionalServiceDto.class);

        return new SuccessDataResult(additionalServiceDto, "Data getted");
    }

    @Override
    public Result update(int id, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.additionalServiceDao.getById(id);

        additionalService.setName(updateAdditionalServiceRequest.getName());
        additionalService.setDailyPrice(updateAdditionalServiceRequest.getDailyPrice());

        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService, AdditionalServiceDto.class);

        this.additionalServiceDao.save(additionalService);

        return new SuccessDataResult(additionalServiceDto, "Data updated, new data: ");
    }

    @Override
    public Result delete(int id) throws BusinessException {

        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(this.additionalServiceDao.getById(id), AdditionalServiceDto.class);
        this.additionalServiceDao.deleteById(id);
        return new SuccessDataResult(additionalServiceDto, "Data deleted");
    }
}
