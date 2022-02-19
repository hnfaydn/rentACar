package com.turkcell.rentACar.api.controllers;

import java.util.List;

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
    public DataResult<List<CarListDto>> getAll() {
        return this.carService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateCarRequest createCarRequest){
        return this.carService.add(createCarRequest);
    }

    @GetMapping("/getbyid")
    public DataResult<CarDto> getById(@RequestParam(required = true) int id){
        return this.carService.getById(id);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam int id){
        return this.carService.delete(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody UpdateCarRequest updateCarRequest){
        return this.carService.update(id, updateCarRequest);
    }
    @GetMapping("/findByDailyPriceLessThanEqual")
    DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice){
    	return this.carService.findByDailyPriceLessThanEqual(dailyPrice);
    }
    @GetMapping("/getAllPaged")
    DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize){
    	return this.carService.getAllPaged(pageNo, pageSize);
    }
    @GetMapping("/getAllSortedByDailyPrice")
    DataResult<List<CarListDto>> getAllSortedByDailyPrice(String sortType){
    	return this.carService.getAllSortedByDailyPrice(sortType);
    }

}
