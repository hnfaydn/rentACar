package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CityService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityListDto;
import com.turkcell.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.turkcell.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CityDao;
import com.turkcell.rentACar.entities.concretes.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityManager implements CityService {

    private CityDao cityDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CityManager(CityDao cityDao,
                       ModelMapperService modelMapperService) {
        this.cityDao = cityDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<CityListDto>> getAll() throws BusinessException {

        List<City> cities = this.cityDao.findAll();

        List<CityListDto> cityListDtos = cities.stream()
                .map(city -> this.modelMapperService.forDto().map(city, CityListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(cityListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateCityRequest createCityRequest) throws BusinessException {

        City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);

        checkIfCityIdNotDuplicated(city.getCityId());
        checkIfNameNotDuplicated(city.getCityName());


        this.cityDao.save(city);

        return new SuccessDataResult(createCityRequest,BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<CityDto> getById(int id) throws BusinessException {

        checkIfCityIdExists(id);

        City city = this.cityDao.getById(id);
        CityDto cityDto = this.modelMapperService.forDto().map(city, CityDto.class);

        return new SuccessDataResult(cityDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateCityRequest updateCityRequest) throws BusinessException {

        checkIfCityIdExists(id);

        City city = this.cityDao.getById(id);

        checkIfNameNotDuplicated(updateCityRequest.getCityName());
        updateCityOperations(city, updateCityRequest);

        CityDto cityDto = this.modelMapperService.forDto().map(city,CityDto.class);

        this.cityDao.save(city);

        return new SuccessDataResult(cityDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfCityIdExists(id);

        CityDto cityDto = this.modelMapperService.forDto().map(this.cityDao.getById(id), CityDto.class);
        this.cityDao.deleteById(id);

        return new SuccessDataResult(cityDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public boolean cityExistsById(int id) {

       return this.cityDao.existsById(id);
    }

    @Override
    public City getCityById(int id) {

        if(!this.cityDao.existsById(id)){
        return null;}

        return this.cityDao.getById(id);
    }


    private void checkIfNameNotDuplicated(String cityName) throws BusinessException {

        if(this.cityDao.existsByCityName(cityName)){
            throw new BusinessException(BusinessMessages.CityMessages.CITY_NAME_ALREADY_EXISTS+cityName);
        }
    }

    private void checkIfCityIdNotDuplicated(int cityId) throws BusinessException {

        if(this.cityDao.existsByCityId(cityId)){
            throw new BusinessException(BusinessMessages.CityMessages.CITY_ID_ALREADY_EXISTS+cityId);
        }
    }

    private void checkIfCityIdExists(int id) throws BusinessException {

        if(!this.cityDao.existsById(id)){
            throw new BusinessException(BusinessMessages.CityMessages.CITY_NOT_FOUND+id);
        }
    }

    private void updateCityOperations(City city, UpdateCityRequest updateCityRequest) {

        city.setCityName(updateCityRequest.getCityName());
    }
}
