package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.CarMaintenanceDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACar.entities.concretes.CarMaintenance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarMaintenanceManager implements CarMaintenanceService {

    private CarMaintenanceDao carMaintenanceDao;
    private ModelMapperService modelMapperService;

    @Override
    public DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException {

        List<CarMaintenance> carMaintenances = carMaintenanceDao.findAll();

        List<CarMaintenanceListDto> carMaintenanceListDtos = carMaintenances.stream()
                .map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class)).collect(Collectors.toList());



        return new SuccessDataResult<>(carMaintenanceListDtos, "Data Listed");
    }

    @Override
    public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

        CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest, CarMaintenance.class);

        this.carMaintenanceDao.save(carMaintenance);
        return new SuccessDataResult(createCarMaintenanceRequest, "Data added");
    }

    @Override
    public DataResult<CarMaintenanceDto> getById(int id) throws BusinessException {
        CarMaintenance carMaintenance = this.carMaintenanceDao.getById(id);

        CarMaintenanceDto carMaintenanceDto = this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceDto.class);
        return new SuccessDataResult(carMaintenanceDto,"Data getted by following id: "+id);
    }

    @Override
    public Result update(int id, UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {
        return null;
    }

    @Override
    public Result delete(int id) throws BusinessException {

        this.carMaintenanceDao.deleteById(id);

        return new SuccessResult("Data Deleted");
    }
}
