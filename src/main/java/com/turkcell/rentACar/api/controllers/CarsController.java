package com.turkcell.rentACar.api.controllers;

import java.util.List;

import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.CarDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.requests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import lombok.AllArgsConstructor;

@RequestMapping("/api/cars")
@RestController
@AllArgsConstructor
public class CarsController {

    private CarService carService;

    @GetMapping("/getall")
    public DataResult<List<CarListDto>> getAll() throws BusinessException {
        return this.carService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateCarRequest createCarRequest) throws BusinessException {
        return this.carService.add(createCarRequest);
    }

    @GetMapping("/getbyid")
    public DataResult<CarDto> getById(@RequestParam int id) throws BusinessException {
        return this.carService.getById(id);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.carService.delete(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody UpdateCarRequest updateCarRequest) throws BusinessException {
        return this.carService.update(id, updateCarRequest);
    }
    @GetMapping("/findByDailyPriceLessThanEqual")
    DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException {
    	return this.carService.findByDailyPriceLessThanEqual(dailyPrice);
    }
    @GetMapping("/getAllPaged")
    DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {
    	return this.carService.getAllPaged(pageNo, pageSize);
    }
    @GetMapping("/getAllSortedByDailyPrice")
    DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) throws BusinessException {
    	return this.carService.getAllSortedByDailyPrice(sortDirection);
    }

}
