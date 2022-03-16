package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorsController {

    private final ColorService colorService;

    @Autowired
    public ColorsController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/getAll")
    public DataResult<List<ColorListDto>> getAll() throws BusinessException {
        return this.colorService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateColorRequest createColorRequest) throws BusinessException {
        return this.colorService.add(createColorRequest);
    }

    @GetMapping("/getById")
    public DataResult<ColorDto> getById(@RequestParam int id) throws BusinessException {
        return this.colorService.getById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestParam int id, @RequestBody @Valid UpdateColorRequest updateColorRequest) throws BusinessException {
        return this.colorService.update(id, updateColorRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam int id) throws BusinessException {
        return this.colorService.delete(id);
    }


}
