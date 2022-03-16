package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenancesController {

    private final CarMaintenanceService carMaintenanceService;

    @Autowired
    public CarMaintenancesController(CarMaintenanceService carMaintenanceService) {
        this.carMaintenanceService = carMaintenanceService;
    }

    @GetMapping("/getAll")
    public DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException {
        return this.carMaintenanceService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {
        return this.carMaintenanceService.add(createCarMaintenanceRequest);
    }

    @GetMapping("/getById")
    public DataResult<CarMaintenanceDto> getById(@RequestParam int id) throws BusinessException {
        return this.carMaintenanceService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {
        return this.carMaintenanceService.update(id, updateCarMaintenanceRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.carMaintenanceService.delete(id);
    }

}
