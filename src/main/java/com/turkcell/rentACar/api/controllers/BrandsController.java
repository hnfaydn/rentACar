package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.BrandDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.requests.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@AllArgsConstructor
public class BrandsController {

    private BrandService brandService;


    @GetMapping("/getall")
    public DataResult<List<BrandListDto>> getAll() {
        return this.brandService.getAll();
    }


    @PostMapping("/add")
    public Result add(@RequestBody CreateBrandRequest createBrandRequest){

       return this.brandService.add(createBrandRequest);

    }

    @GetMapping("/getbyid")
    public DataResult<BrandDto> getById(@RequestParam(required = true) int id){

        return this.brandService.getById(id);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam int id){
       return this.brandService.delete(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody UpdateBrandRequest updateBrandRequest){
       return this.brandService.update(id, updateBrandRequest);
    }

}