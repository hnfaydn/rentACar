package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandService brandService;

    @Autowired
    public BrandsController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/getAll")
    public DataResult<List<BrandListDto>> getAll() throws BusinessException {
        return this.brandService.getAll();
    }


    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateBrandRequest createBrandRequest) throws BusinessException {
        return this.brandService.add(createBrandRequest);
    }

    @GetMapping("/getById")
    public DataResult<BrandDto> getById(@RequestParam int id) throws BusinessException {

        return this.brandService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateBrandRequest updateBrandRequest) throws BusinessException {
        return this.brandService.update(id, updateBrandRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.brandService.delete(id);
    }

}