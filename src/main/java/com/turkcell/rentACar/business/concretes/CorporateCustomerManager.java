package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.turkcell.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorporateCustomerManager implements CorporateCustomerService {

    private CorporateCustomerDao corporateCustomerDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao, ModelMapperService modelMapperService) {
        this.corporateCustomerDao = corporateCustomerDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAll() {

        List<CorporateCustomer> corporateCustomers = this.corporateCustomerDao.findAll();

        List<CorporateCustomerListDto> corporateCustomerListDtos =
                corporateCustomers.stream().map(corporateCustomer -> this.modelMapperService.forDto()
                        .map(corporateCustomer, CorporateCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(corporateCustomerListDtos,"Data Listed Successfully");
    }

    @Override
    public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {
        CorporateCustomer corporateCustomer = this.modelMapperService.forDto().map(createCorporateCustomerRequest, CorporateCustomer.class);

        this.corporateCustomerDao.save(corporateCustomer);

        return new SuccessDataResult(createCorporateCustomerRequest,"Data Added Successfully.");
    }

    @Override
    public DataResult<CorporateCustomerDto> getById(int id) {

        CorporateCustomer corporateCustomer = this.corporateCustomerDao.getById(id);

        CorporateCustomerDto corporateCustomerDto = this.modelMapperService.forDto().map(corporateCustomer,CorporateCustomerDto.class);

        return new SuccessDataResult(corporateCustomerDto,"Data Brought Successfully.");
    }

    @Override
    public Result delete(int id) {
        CorporateCustomerDto corporateCustomerDto = this.modelMapperService.forDto().map(this.corporateCustomerDao.getById(id), CorporateCustomerDto.class);
        this.corporateCustomerDao.deleteById(id);

        return new SuccessDataResult(corporateCustomerDto,"Data Deleted Successfully.");
    }

    @Override
    public Result update(int id, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {
        return null;
    }
}
