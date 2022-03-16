package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.UserService;
import com.turkcell.rentACar.business.dtos.userDtos.UserDto;
import com.turkcell.rentACar.business.dtos.userDtos.UserListDto;
import com.turkcell.rentACar.business.requests.userRequests.UpdateUserRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.UserDao;
import com.turkcell.rentACar.entities.concretes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManager implements UserService {

    private UserDao userDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public UserManager(UserDao userDao,
                       ModelMapperService modelMapperService) {
        this.userDao = userDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<UserListDto>> getAll() {

        List<User> users = this.userDao.findAll();

        List<UserListDto> userListDtos = users.stream()
                .map(user -> this.modelMapperService.forDto().map(user,UserListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(userListDtos,"Data listed successfully");
    }

    @Override
    public DataResult<UserDto> getById(int id) throws BusinessException {

        checkIfUserIdExists(id);

        User user = this.userDao.getById(id);

        UserDto userDto = this.modelMapperService.forDto().map(user, UserDto.class);

        return new SuccessDataResult(userDto,"Data Brought Successfully:");
    }



    @Override
    public Result update(int id, UpdateUserRequest updateUserRequest) throws BusinessException {

        checkIfUserIdExists(id);
        checkIfEmailAlreadyExists(updateUserRequest.getEmail());

        User user = this.userDao.getById(id);

        user.setEmail(updateUserRequest.getEmail());
        user.setPassword(updateUserRequest.getPassword());

        UserDto userDto = this.modelMapperService.forDto().map(user,UserDto.class);

        return new SuccessDataResult(userDto,"User email and password updated successfully.");
    }

    private void checkIfEmailAlreadyExists(String email) throws BusinessException {
        if(this.userDao.existsUserByEmail(email)){
           throw new BusinessException("This email is already using by another user: "+email);
        }
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfUserIdExists(id);

        UserDto userDto = this.modelMapperService.forDto().map(this.userDao.getById(id), UserDto.class);

        this.userDao.deleteById(id);

        return new SuccessDataResult(userDto,"Data Deleted: ");
    }

    @Override
    public boolean checkIfUserEmailAlreadyExists(String email) {
        return this.userDao.existsUserByEmail(email);
    }

    private void checkIfUserIdExists(int id) throws BusinessException {
        if(!this.userDao.existsById(id)){
            throw new BusinessException("There is no user with following id: "+id);
        }
    }
}
