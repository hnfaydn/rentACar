package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationDto;
import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationListDto;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.CreateUserCardInformationRequest;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.UpdateUserCardInformationRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface UserCardInformationService {

    DataResult<List<UserCardInformationListDto>> getAll() throws BusinessException;

    Result add(CreateUserCardInformationRequest createUserCardInformationRequest) throws BusinessException;

    DataResult<UserCardInformationDto> getById(int id) throws BusinessException;

    Result update(int id, UpdateUserCardInformationRequest updateUserCardInformationRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;
}
