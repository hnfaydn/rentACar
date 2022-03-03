package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.CarMaintenanceDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carMaintenance")
@AllArgsConstructor
public class CarMaintenancesController {

    private CarMaintenanceService carMaintenanceService;

    @GetMapping("/getall")
    public DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException{
        return this.carMaintenanceService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException{
        return this.carMaintenanceService.add(createCarMaintenanceRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id,@RequestBody UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException{
        return this.carMaintenanceService.update(id,updateCarMaintenanceRequest);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException{
        return this.carMaintenanceService.delete(id);
    }

    @GetMapping("/getbyid")
    public DataResult<CarMaintenanceDto> getById(@RequestParam int id) throws BusinessException{
        return this.carMaintenanceService.getById(id);
    }
}
