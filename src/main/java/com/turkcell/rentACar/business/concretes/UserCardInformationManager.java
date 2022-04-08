package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.UserCardInformationService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationDto;
import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationListDto;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.CreateUserCardInformationRequest;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.UpdateUserCardInformationRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.UserCardInformationDao;
import com.turkcell.rentACar.entities.concretes.UserCardInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCardInformationManager implements UserCardInformationService {

    private UserCardInformationDao userCardInformationDao;
    private ModelMapperService modelMapperService;
    private CustomerService customerService;

    @Autowired
    public UserCardInformationManager(UserCardInformationDao userCardInformationDao,
                                      ModelMapperService modelMapperService,
                                      CustomerService customerService) {
        this.userCardInformationDao = userCardInformationDao;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @Override
    public DataResult<List<UserCardInformationListDto>> getAll() throws BusinessException {

        List<UserCardInformationListDto> userCardInformationListDtos =
                this.userCardInformationDao.findAll().stream()
                .map(userCardInformation ->this.modelMapperService.forDto().map(userCardInformation,UserCardInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(userCardInformationListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreateUserCardInformationRequest createUserCardInformationRequest) throws BusinessException {

        checkIfCardNoAlreadyExists(createUserCardInformationRequest.getPaymentInformations().getCardNo());
        checkIfCustomerIdExists(createUserCardInformationRequest.getCustomerId());

        UserCardInformation userCardInformation = this.modelMapperService.forRequest().map(createUserCardInformationRequest,UserCardInformation.class);

        userCardInformation.setUserCardInformationId(0);
        this.userCardInformationDao.save(userCardInformation);

        return new SuccessDataResult(createUserCardInformationRequest, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<UserCardInformationDto> getById(int id) throws BusinessException {

        checkIfUserCardInformationIdAExists(id);

        UserCardInformationDto userCardInformationDto = this.modelMapperService.forDto().map(this.userCardInformationDao.getById(id),UserCardInformationDto.class);

        return new SuccessDataResult<>(userCardInformationDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result update(int id, UpdateUserCardInformationRequest updateUserCardInformationRequest) throws BusinessException {

        checkIfUserCardInformationIdAExists(id);
        checkIfCardNoAlreadyExists(updateUserCardInformationRequest.getPaymentInformations().getCardNo());
        checkIfCustomerIdExists(updateUserCardInformationRequest.getCustomerId());

        UserCardInformation userCardInformation = this.userCardInformationDao.getById(id);

        updateUserCardInformationOperations(userCardInformation,updateUserCardInformationRequest);

        UserCardInformationDto userCardInformationDto = this.modelMapperService.forDto().map(userCardInformation,UserCardInformationDto.class);

        return new SuccessDataResult(userCardInformationDto,BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfUserCardInformationIdAExists(id);

        UserCardInformationDto userCardInformationDto = this.modelMapperService.forDto()
                .map(this.userCardInformationDao.getById(id),UserCardInformationDto.class);

        this.userCardInformationDao.deleteById(id);

        return new SuccessDataResult(userCardInformationDto,BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    private void checkIfCardNoAlreadyExists(String cardNo) throws BusinessException {

        if(this.userCardInformationDao.existsUserCardInformationByUserCardInformationId(cardNo)){
            throw new BusinessException(BusinessMessages.UserCardInformationMessages.CARD_NO_ALREADY_EXISTS);
        }
    }

    private void checkIfCustomerIdExists(int customerId) throws BusinessException {

        if(this.customerService.getCustomerById(customerId)==null){
            throw new BusinessException(BusinessMessages.CustomerMessages.CUSTOMER_NOT_FOUND);
        }
    }

    private void checkIfUserCardInformationIdAExists(int id) throws BusinessException {

        if(!this.userCardInformationDao.existsById(id)){
            throw new BusinessException(BusinessMessages.UserCardInformationMessages.USER_CARD_INFORMATION_ID_NOT_FOUND);
        }
    }

    private void updateUserCardInformationOperations(UserCardInformation userCardInformation, UpdateUserCardInformationRequest updateUserCardInformationRequest) {

        userCardInformation.setCardNo(updateUserCardInformationRequest.getPaymentInformations().getCardNo());
        userCardInformation.setCardHolder(updateUserCardInformationRequest.getPaymentInformations().getCardHolder());
        userCardInformation.setExpirationYear(updateUserCardInformationRequest.getPaymentInformations().getExpirationYear());
        userCardInformation.setExpirationMonth(updateUserCardInformationRequest.getPaymentInformations().getExpirationMonth());
        userCardInformation.setCvv(updateUserCardInformationRequest.getPaymentInformations().getCvv());
        userCardInformation.getCustomer().setUserId(updateUserCardInformationRequest.getCustomerId());
    }
}
