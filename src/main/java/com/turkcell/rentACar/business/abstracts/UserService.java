package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.userDtos.UserDto;
import com.turkcell.rentACar.business.dtos.userDtos.UserListDto;
import com.turkcell.rentACar.business.requests.userRequests.UpdateUserRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface UserService {

    DataResult<List<UserListDto>> getAll();

    DataResult<UserDto> getById(int id);

    Result update(int id, UpdateUserRequest updateUserRequest);

    Result delete(int id) throws BusinessException;

}
