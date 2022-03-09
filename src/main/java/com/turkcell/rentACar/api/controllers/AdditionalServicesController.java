package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additinalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/additionalServicesController")
public class AdditionalServicesController {

    private AdditionalServiceService additionalServiceService;

    @Autowired
    public AdditionalServicesController(AdditionalServiceService additionalServiceService) {
        this.additionalServiceService = additionalServiceService;
    }

    @GetMapping("/getAll")
    DataResult<List<AdditionalServiceListDto>> getAll() throws BusinessException{
       return this.additionalServiceService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException{
        return this.additionalServiceService.add(createAdditionalServiceRequest);
    }

    @GetMapping("/getById")
    DataResult<AdditionalServiceDto> getById(@RequestParam int id) throws BusinessException{
        return this.additionalServiceService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id,@RequestBody UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException{
        return this.additionalServiceService.update(id,updateAdditionalServiceRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException{
        return this.additionalServiceService.delete(id);
    }
}
