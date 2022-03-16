package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.UserService;
import com.turkcell.rentACar.business.dtos.userDtos.UserDto;
import com.turkcell.rentACar.business.dtos.userDtos.UserListDto;
import com.turkcell.rentACar.business.requests.userRequests.UpdateUserRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    DataResult<List<UserListDto>> getAll(){
        return this.userService.getAll();
    }

    @GetMapping("/getById")
    DataResult<UserDto> getById(@RequestParam int id) throws BusinessException {
        return this.userService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id, @RequestBody @Valid UpdateUserRequest updateUserRequest) throws BusinessException {
        return this.userService.update(id,updateUserRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException{
        return this.userService.delete(id);
    }
}
