package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.ColorDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.requests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACar.entities.concretes.Color;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ColorManager implements ColorService {

    private ColorDao colorDao;
    private ModelMapperService modelMapperService;


    @Override
    public DataResult<List<ColorListDto>> getAll() {
        List<Color> colors = this.colorDao.findAll();

        List<ColorListDto> colorListDtos = colors.stream()
                .map(color -> this.modelMapperService.forDto()
                        .map(color, ColorListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<ColorListDto>>(colorListDtos,"Data listed");
    }

    @Override
    public Result add(CreateColorRequest createColorRequest){
        
            Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
            if(!checkIfNameNotDuplicated(color.getName()).isSuccess()) {
                return new ErrorDataResult(createColorRequest,"This color is already exists : " + color.getName());

            }

        this.colorDao.save(color);
        return new SuccessDataResult(createColorRequest,"Data added : " + color.getName());

    }

    @Override
    public DataResult<ColorDto> getById(int id){
       
        if(!checkIfIdExist(id).isSuccess()) {
            return new ErrorDataResult("There is no colour with the following id : " + id);
        }

        Color color = this.colorDao.findById(id);
        ColorDto colorDto = this.modelMapperService.forDto().map(color, ColorDto.class);
        return new SuccessDataResult<ColorDto>(colorDto,"Data getted");

    }


    @Override
    public Result update(int id, UpdateColorRequest updateColorRequest){
      
            
        if(!checkIfIdExist(id).isSuccess()) {
            return new ErrorResult("There is no data with following id : " + id);
		}

        Color color = this.colorDao.getById(id);

        if(!checkIfNameNotDuplicated(updateColorRequest.getName()).isSuccess()) {
            return new ErrorResult("This color is already exists " + updateColorRequest.getName());
        }
        String colorNameBeforeUpdate = this.colorDao.findById(id).getName();
        updateColorOperations(color, updateColorRequest);
        this.colorDao.save(color);
        return new SuccessResult(colorNameBeforeUpdate +" updated to " +updateColorRequest.getName());

    }

    @Override
    public Result delete(int id){

        if(!checkIfIdExist(id).isSuccess()) {
            return new ErrorResult("There is no data with following id : " + id);
        }
        String colorNameBeforeDeleted = this.colorDao.getById(id).getName();
        this.colorDao.deleteById(id);
        return new SuccessResult("Data deleted : " + colorNameBeforeDeleted );

    }

    private void updateColorOperations(Color color, UpdateColorRequest updateColorRequest) {
        color.setName(updateColorRequest.getName());
    }

    private Result checkIfNameNotDuplicated(String name){

        if (this.colorDao.existsByName(name)) {
           return new ErrorResult("This color is already exist in system!");
        }
        return new SuccessResult();
    }

    private Result checkIfIdExist(int id){
        if (!this.colorDao.existsById(id)) {
             return new ErrorResult("There is no color with this id: " + id);
        }
        return new SuccessResult();
    }

}