package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.entities.concretes.Brand;
import lombok.SneakyThrows;
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
    public DataResult<List<ColorListDto>> getAll() throws BusinessException {
        List<Color> colors = this.colorDao.findAll();

        checkIfColorListEmpty(colors);

        List<ColorListDto> colorListDtos = colors.stream().map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(colorListDtos, "Data listed");
    }


    @Override
    public Result add(CreateColorRequest createColorRequest) throws BusinessException {

        Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);

        checkIfNameNotNull(color.getName());

        checkIfNameNotDuplicated(color.getName());

        this.colorDao.save(color);
        return new SuccessDataResult(createColorRequest, "Data added : " + color.getName());
    }


    @Override
    public DataResult<ColorDto> getById(int id) throws BusinessException {

        checkIfIdExist(id);

        Color color = this.colorDao.findById(id);
        ColorDto colorDto = this.modelMapperService.forDto().map(color, ColorDto.class);
        return new SuccessDataResult<>(colorDto, "Data getted");
    }


    @Override
    public Result update(int id, UpdateColorRequest updateColorRequest) throws BusinessException {

        checkIfIdExist(id);


        Color color = this.colorDao.getById(id);

        checkIfNameNotNull(updateColorRequest.getName());

        checkIfNameNotDuplicated(updateColorRequest.getName());

        String colorNameBeforeUpdate = this.colorDao.findById(id).getName();

        updateColorOperations(color, updateColorRequest);

        this.colorDao.save(color);
        return new SuccessResult(colorNameBeforeUpdate + " updated to " + updateColorRequest.getName());
    }


    @Override
    public Result delete(int id) throws BusinessException {
        checkIfIdExist(id);

        String colorNameBeforeDeleted = this.colorDao.getById(id).getName();
        this.colorDao.deleteById(id);
        return new SuccessResult("Data deleted : " + colorNameBeforeDeleted);
    }


    private void updateColorOperations(Color color, UpdateColorRequest updateColorRequest) {
        color.setName(updateColorRequest.getName());
    }

    private void checkIfNameNotDuplicated(String name) throws BusinessException {
        if (this.colorDao.existsByName(name)) {
            throw new BusinessException("This color is already exist in system: " + name);
        }
    }

    private void checkIfIdExist(int id) throws BusinessException {
        if (!this.colorDao.existsById(id)) {
            throw new BusinessException("There is no color with this id: " + id);
        }
    }

    private void checkIfNameNotNull(String colorName) throws BusinessException {
        if (colorName.isEmpty() || colorName.isBlank()) {
            throw new BusinessException("Color name can not empty or null!");
        }
    }

    private void checkIfColorListEmpty(List<Color> colors) throws BusinessException {
        if (colors.isEmpty()) {
            throw new BusinessException("There is no Color to list");
        }
    }

}