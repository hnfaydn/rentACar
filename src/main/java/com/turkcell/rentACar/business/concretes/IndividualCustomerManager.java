package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACar.business.abstracts.UserService;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.IndividualCustomerDao;
import com.turkcell.rentACar.entities.concretes.IndividualCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

    private IndividualCustomerDao individualCustomerDao;
    private ModelMapperService modelMapperService;
    private UserService userService;

    @Autowired
    public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao,
                                     ModelMapperService modelMapperService,
                                     UserService userService) {
        this.individualCustomerDao = individualCustomerDao;
        this.modelMapperService = modelMapperService;
        this.userService = userService;
    }

    @Override
    public DataResult<List<IndividualCustomerListDto>> getAll() {

        List<IndividualCustomer> individualCustomers = this.individualCustomerDao.findAll();

        List<IndividualCustomerListDto> individualCustomerListDtos = individualCustomers.stream()
                .map(individualCustomer -> this.modelMapperService.forDto()
                        .map(individualCustomer,IndividualCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(individualCustomerListDtos,"Data Listed Successfully.");
    }

    @Override
    public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException {

        checkIfIndividualCustomerEmailAlreadyExists(createIndividualCustomerRequest.getEmail());
        checkIfIndividualCustomerNationalIdentityAlreadyExists(createIndividualCustomerRequest.getNationalIdentity());

        IndividualCustomer individualCustomer = this.modelMapperService.forDto().map(createIndividualCustomerRequest, IndividualCustomer.class);

        this.individualCustomerDao.save(individualCustomer);

        return new SuccessDataResult(createIndividualCustomerRequest,"Data Added Successfully.");
    }




    @Override
    public DataResult<IndividualCustomerDto> getById(int id) throws BusinessException {

        checkIfIndividualCustomerIdExists(id);

        IndividualCustomer individualCustomer = this.individualCustomerDao.getById(id);

        IndividualCustomerDto individualCustomerDto = this.modelMapperService.forDto().map(individualCustomer, IndividualCustomerDto.class);

        return new SuccessDataResult(individualCustomerDto,"Data Brought Successfully.");
    }



    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIndividualCustomerIdExists(id);

        IndividualCustomerDto individualCustomerDto = this.modelMapperService.forDto().map(this.individualCustomerDao.getById(id),IndividualCustomerDto.class);

        this.individualCustomerDao.deleteById(id);

        return new SuccessDataResult(individualCustomerDto,"Data DeletedSuccessfully");
    }

    @Override
    public Result update(int id, UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException {

        checkIfIndividualCustomerIdExists(id);
        checkIfIndividualCustomerEmailAlreadyExists(updateIndividualCustomerRequest.getEmail());
        checkIfIndividualCustomerNationalIdentityAlreadyExists(updateIndividualCustomerRequest.getNationalIdentity());

        IndividualCustomer individualCustomer = this.individualCustomerDao.getById(id);

        individualCustomerUpdateOperations(individualCustomer,updateIndividualCustomerRequest);

        IndividualCustomerDto individualCustomerDto = this.modelMapperService.forDto().map(individualCustomer,IndividualCustomerDto.class);

        return new SuccessDataResult(individualCustomerDto,"Data updated successfully");
    }

    private void individualCustomerUpdateOperations(IndividualCustomer individualCustomer, UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {

        individualCustomer.setEmail(updateIndividualCustomerRequest.getEmail());
        individualCustomer.setFirstName(updateIndividualCustomerRequest.getFirstName());
        individualCustomer.setLastName(updateIndividualCustomerRequest.getLastName());
        individualCustomer.setNationalIdentity(updateIndividualCustomerRequest.getNationalIdentity());

    }

    private void checkIfIndividualCustomerEmailAlreadyExists(String email) throws BusinessException {
        if(this.userService.checkIfUserEmailAlreadyExists(email)){
            throw new BusinessException("There is customer with following email: " +email);
        }
    }

    private void checkIfIndividualCustomerNationalIdentityAlreadyExists(String nationalIdentity) throws BusinessException {
        if(this.individualCustomerDao.existsIndividualCustomerByNationalIdentity(nationalIdentity)){
            throw new BusinessException("There is individual customer with following national id: " +nationalIdentity);
        }
    }

    private void checkIfIndividualCustomerIdExists(int id) throws BusinessException {
        if(this.individualCustomerDao.existsById(id)){
            throw new BusinessException("There no individual customer with following id: "+id);
        }
    }
}
