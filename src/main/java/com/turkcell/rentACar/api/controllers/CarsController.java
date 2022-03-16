package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarListDto;
import com.turkcell.rentACar.business.requests.carRequests.CreateCarRequest;
import com.turkcell.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/cars")
public class CarsController {

    private final CarService carService;

    @Autowired
    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/getAll")
    public DataResult<List<CarListDto>> getAll() throws BusinessException {
        return this.carService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarRequest createCarRequest) throws BusinessException {
        return this.carService.add(createCarRequest);
    }

    @GetMapping("/getById")
    public DataResult<CarDto> getById(@RequestParam int id) throws BusinessException {
        return this.carService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateCarRequest updateCarRequest) throws BusinessException {
        return this.carService.update(id, updateCarRequest);
    }

    @GetMapping("/findByDailyPriceLessThanEqual")
    DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(@RequestParam double dailyPrice) throws BusinessException {
        return this.carService.findByDailyPriceLessThanEqual(dailyPrice);
    }

    @GetMapping("/getAllPaged")
    DataResult<List<CarListDto>> getAllPaged(@RequestParam int pageNo,@RequestParam int pageSize) throws BusinessException {
        return this.carService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getAllSortedByDailyPrice")
    DataResult<List<CarListDto>> getAllSortedByDailyPrice(Sort.Direction sortDirection) throws BusinessException {
        return this.carService.getAllSortedByDailyPrice(sortDirection);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.carService.delete(id);
    }
}
