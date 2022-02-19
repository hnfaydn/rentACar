package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.ColorDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.requests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@AllArgsConstructor
public class ColorsController {

    private ColorService colorService;


    @GetMapping("/getall")
    public DataResult<List<ColorListDto>> getAll() {
        return this.colorService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateColorRequest createColorRequest){
        return this.colorService.add(createColorRequest);
    }

    @GetMapping("/getbyid")
    public DataResult<ColorDto> getById(@RequestParam(required = true) int id){
        return this.colorService.getById(id);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam int id){
       return this.colorService.delete(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody UpdateColorRequest updateColorRequest){
       return this.colorService.update(id, updateColorRequest);
    }


}
