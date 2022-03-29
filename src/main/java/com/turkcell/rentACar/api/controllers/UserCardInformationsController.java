package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.UserCardInformationService;
import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationDto;
import com.turkcell.rentACar.business.dtos.userCardInformationDto.UserCardInformationListDto;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.CreateUserCardInformationRequest;
import com.turkcell.rentACar.business.requests.userCardInformationRequests.UpdateUserCardInformationRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userCardInformationsController")
@CrossOrigin
public class UserCardInformationsController {

    private UserCardInformationService userCardInformationService;

    @Autowired
    public UserCardInformationsController(UserCardInformationService userCardInformationService) {
        this.userCardInformationService = userCardInformationService;
    }

    @GetMapping("/getAll")
    DataResult<List<UserCardInformationListDto>> getAll() throws BusinessException{
        return this.userCardInformationService.getAll();
    }

    @PostMapping("/add")
    Result add(CreateUserCardInformationRequest createUserCardInformationRequest) throws BusinessException{
        return this.userCardInformationService.add(createUserCardInformationRequest);
    }

    @GetMapping("/getById")
    DataResult<UserCardInformationDto> getById(int id) throws BusinessException{
        return this.userCardInformationService.getById(id);
    }

    @PutMapping("/update")
    Result update(int id, UpdateUserCardInformationRequest updateUserCardInformationRequest) throws BusinessException{
        return this.userCardInformationService.update(id,updateUserCardInformationRequest);
    }

    @DeleteMapping("/delete")
    Result delete(int id) throws BusinessException{
        return this.userCardInformationService.delete(id);
    }
}
