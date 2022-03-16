package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.UserService;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.turkcell.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorporateCustomerManager implements CorporateCustomerService {

    private CorporateCustomerDao corporateCustomerDao;
    private ModelMapperService modelMapperService;
    private CustomerService customerService;
    private UserService userService;

    @Autowired
    public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao,
                                    ModelMapperService modelMapperService,
                                    @Lazy CustomerService customerService,
                                    @Lazy UserService userService) {
        this.corporateCustomerDao = corporateCustomerDao;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
        this.userService = userService;
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
    public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerEmailAlreadyExists(createCorporateCustomerRequest.getEmail());
        checkIfCorporateCustomerTaxNumberAlreadyExists(createCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.modelMapperService.forDto().map(createCorporateCustomerRequest, CorporateCustomer.class);

        this.corporateCustomerDao.save(corporateCustomer);

        return new SuccessDataResult(createCorporateCustomerRequest,"Data Added Successfully.");
    }

    @Override
    public DataResult<CorporateCustomerDto> getById(int id) throws BusinessException {

        checkIfCorporateCustomerIdExists(id);

        CorporateCustomer corporateCustomer = this.corporateCustomerDao.getById(id);

        CorporateCustomerDto corporateCustomerDto = this.modelMapperService.forDto().map(corporateCustomer,CorporateCustomerDto.class);

        return new SuccessDataResult(corporateCustomerDto,"Data Brought Successfully.");
    }



    @Override
    public Result delete(int id) throws BusinessException {

        checkIfCorporateCustomerIdExists(id);

        CorporateCustomerDto corporateCustomerDto = this.modelMapperService.forDto().map(this.corporateCustomerDao.getById(id), CorporateCustomerDto.class);
        this.corporateCustomerDao.deleteById(id);

        return new SuccessDataResult(corporateCustomerDto,"Data Deleted Successfully.");
    }

    @Override
    public Result update(int id, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerIdExists(id);
        checkIfCorporateCustomerEmailAlreadyExists(updateCorporateCustomerRequest.getEmail());
        checkIfCorporateCustomerTaxNumberAlreadyExists(updateCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.corporateCustomerDao.getById(id);

        corporateCustomerUpdateOperations(corporateCustomer,updateCorporateCustomerRequest);

        CorporateCustomerDto corporateCustomerDto = this.modelMapperService.forDto().map(corporateCustomer,CorporateCustomerDto.class);

        this.corporateCustomerDao.save(corporateCustomer);

        return new SuccessDataResult(corporateCustomerDto,"Data Updated to following data Successfully");
    }

    private void corporateCustomerUpdateOperations(CorporateCustomer corporateCustomer, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {

        corporateCustomer.setEmail(updateCorporateCustomerRequest.getEmail());
        corporateCustomer.setPassword(updateCorporateCustomerRequest.getPassword());
        corporateCustomer.setCompanyName(updateCorporateCustomerRequest.getCompanyName());
        corporateCustomer.setTaxNumber(updateCorporateCustomerRequest.getTaxNumber());
    }

    private void checkIfCorporateCustomerIdExists(int id) throws BusinessException {
        if (!this.corporateCustomerDao.existsById(id)) {
            throw new BusinessException("There is no corporate customer with following id: "+id);
        }
    }

    private void checkIfCorporateCustomerEmailAlreadyExists(String email) throws BusinessException {
        if(this.userService.checkIfUserEmailAlreadyExists(email)){
            throw new BusinessException("There is customer with following email: " +email);
        }
    }

    private void checkIfCorporateCustomerTaxNumberAlreadyExists(String taxNumber) throws BusinessException {
        if (this.corporateCustomerDao.existsCorporateCustomerByTaxNumber(taxNumber)){
            throw new BusinessException("There is corporate customer with following tax number: " +taxNumber);
        }
    }
}
