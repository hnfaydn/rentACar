package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orderedAdditionalServicesController")
public class OrderedAdditionalServicesController {

    private OrderedAdditionalServiceService orderedAdditionalServiceService;

    @Autowired
    public OrderedAdditionalServicesController(OrderedAdditionalServiceService orderedAdditionalServiceService) {
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
    }

    @GetMapping("/getAll")
    DataResult<List<OrderedAdditionalServiceListDto>> getAll() throws BusinessException {
        return this.orderedAdditionalServiceService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException{
        return this.orderedAdditionalServiceService.add(createOrderedAdditionalServiceRequest);
    }

    @GetMapping("/getById")
    DataResult<OrderedAdditionalServiceDto> getById(@RequestParam int id) throws BusinessException{
        return this.orderedAdditionalServiceService.getById(id);
    }

    @PutMapping("/update")
    Result update(@RequestParam int id,@RequestBody UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException{
        return this.orderedAdditionalServiceService.update(id,updateOrderedAdditionalServiceRequest);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException{
        return this.orderedAdditionalServiceService.delete(id);
    }
}
