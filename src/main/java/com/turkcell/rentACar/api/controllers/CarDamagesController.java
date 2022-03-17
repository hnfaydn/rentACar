package com.turkcell.rentACar.api.controllers;


import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageDto;
import com.turkcell.rentACar.business.dtos.carDamageDtos.CarDamageListDto;
import com.turkcell.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.turkcell.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carDamagesController")
public class CarDamagesController {

    private CarDamageService carDamageService;

    @Autowired
    public CarDamagesController(CarDamageService carDamageService) {
        this.carDamageService = carDamageService;
    }

    @GetMapping("/getAll")
    public DataResult<List<CarDamageListDto>> getAll() throws BusinessException {
        return this.carDamageService.getAll();
    }


    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarDamageRequest createCarDamageRequest) throws BusinessException {
        return this.carDamageService.add(createCarDamageRequest);
    }

    @GetMapping("/getById")
    public DataResult<CarDamageDto> getById(@RequestParam int id) throws BusinessException {

        return this.carDamageService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateCarDamageRequest updateCarDamageRequest) throws BusinessException {
        return this.carDamageService.update(id, updateCarDamageRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.carDamageService.delete(id);
    }
}
