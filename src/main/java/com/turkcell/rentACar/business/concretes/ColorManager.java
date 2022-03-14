package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACar.entities.concretes.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorManager implements ColorService {

    private final ColorDao colorDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService) {
        this.colorDao = colorDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<ColorListDto>> getAll() throws BusinessException {

        List<Color> colors = this.colorDao.findAll();

        List<ColorListDto> colorListDtos = colors.stream().map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(colorListDtos, "Data listed");
    }

    @Override
    public Result add(CreateColorRequest createColorRequest) throws BusinessException {

        Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);

        checkIfNameNotDuplicated(color.getName());

        this.colorDao.save(color);

        return new SuccessDataResult(createColorRequest, "Data added : " + color.getName());
    }

    @Override
    public DataResult<ColorDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        Color color = this.colorDao.findById(id);
        ColorDto colorDto = this.modelMapperService.forDto().map(color, ColorDto.class);

        return new SuccessDataResult<>(colorDto, "Data getted");
    }

    @Override
    public Result update(int id, UpdateColorRequest updateColorRequest) throws BusinessException {

        checkIfIdExists(id);

        Color color = this.colorDao.getById(id);

        checkIfNameNotDuplicated(updateColorRequest.getName());
        String colorNameBeforeUpdate = this.colorDao.findById(id).getName();
        updateColorOperations(color, updateColorRequest);

        this.colorDao.save(color);

        return new SuccessResult(colorNameBeforeUpdate + " updated to " + updateColorRequest.getName());
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);

        ColorDto colorDto = this.modelMapperService.forDto().map(this.colorDao.getById(id),ColorDto.class);
        this.colorDao.deleteById(id);

        return new SuccessDataResult(colorDto,"Data deleted: ");
    }


    private void updateColorOperations(Color color, UpdateColorRequest updateColorRequest) {

        color.setName(updateColorRequest.getName());
    }

    private void checkIfNameNotDuplicated(String name) throws BusinessException {

        if (this.colorDao.existsByName(name)) {
            throw new BusinessException("This color is already exist in system: " + name);
        }
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.colorDao.existsById(id)) {
            throw new BusinessException("There is no color with this id: " + id);
        }
    }
}