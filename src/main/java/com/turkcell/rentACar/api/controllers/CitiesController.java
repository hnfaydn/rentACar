package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CityService;
import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityListDto;
import com.turkcell.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.turkcell.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CitiesController {

    private CityService cityService;

    @Autowired
    public CitiesController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/getAll")
    public DataResult<List<CityListDto>> getAll() throws BusinessException{
        return this.cityService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCityRequest createCityRequest) throws BusinessException{
        return this.cityService.add(createCityRequest);
    }

    @GetMapping("/getById")
    public DataResult<CityDto> getById(@RequestParam int id) throws BusinessException{
        return this.cityService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id,@RequestBody @Valid UpdateCityRequest updateCityRequest) throws BusinessException{
        return this.cityService.update(id,updateCityRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException{
        return this.cityService.delete(id);
    }
}
