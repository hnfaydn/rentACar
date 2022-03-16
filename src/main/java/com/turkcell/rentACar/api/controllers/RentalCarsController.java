package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rentalCars")
public class RentalCarsController {

    private final RentalCarService rentalCarService;

    @Autowired
    public RentalCarsController(RentalCarService rentalCarService) {
        this.rentalCarService = rentalCarService;
    }

    @GetMapping("/getAll")
    public DataResult<List<RentalCarListDto>> getAll() throws BusinessException {
        return this.rentalCarService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        return this.rentalCarService.add(createRentalCarRequest);
    }

    @GetMapping("/getById")
    public DataResult<RentalCarDto> getById(@RequestParam int id) throws BusinessException {
        return this.rentalCarService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        return this.rentalCarService.update(id, updateRentalCarRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.rentalCarService.delete(id);
    }
}
