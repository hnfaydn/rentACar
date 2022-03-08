package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACar.entities.concretes.CarMaintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarMaintenanceManager implements CarMaintenanceService {

    private final CarMaintenanceDao carMaintenanceDao;
    private final RentalCarService rentalCarService;
    private final CarService carService;
    private final ModelMapperService modelMapperService;

    @Autowired
    @Lazy
    public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, RentalCarService rentalCarService, ModelMapperService modelMapperService, CarService carService) {
        this.carMaintenanceDao = carMaintenanceDao;
        this.rentalCarService = rentalCarService;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException {

        List<CarMaintenance> carMaintenances = carMaintenanceDao.findAll();

        checkIfCarMaintenanceListEmpty(carMaintenances);

        List<CarMaintenanceListDto> carMaintenanceListDtos = carMaintenances.stream()
                .map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(carMaintenanceListDtos, "Data Listed");
    }


    @Override
    public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

        checkIfCarExist(createCarMaintenanceRequest);

        checkIfCarIsRental(createCarMaintenanceRequest);

        CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest, CarMaintenance.class);
        carMaintenance.setCarMaintenanceId(0);
        this.carMaintenanceDao.save(carMaintenance);
        return new SuccessDataResult(createCarMaintenanceRequest, "Data added");
    }


    @Override
    public DataResult<CarMaintenanceDto> getById(int id) throws BusinessException {

        checkIfIdExist(id);

        CarMaintenance carMaintenance = this.carMaintenanceDao.getById(id);

        CarMaintenanceDto carMaintenanceDto = this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceDto.class);
        return new SuccessDataResult(carMaintenanceDto, "Data getted by following id: " + id);
    }

    @Override
    public Result update(int id, UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {

        checkIfIdExist(id);

        CarMaintenance carMaintenance = this.carMaintenanceDao.getById(id);

        checkIfUpdateParametersNotEqual(carMaintenance, updateCarMaintenanceRequest);

        updateCarMaintenanceOperations(carMaintenance, updateCarMaintenanceRequest);

        CarMaintenanceDto carMaintenanceDto = this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceDto.class);

        this.carMaintenanceDao.save(carMaintenance);
        return new SuccessDataResult(carMaintenanceDto, "Data updated, new data: ");
    }


    @Override
    public Result delete(int id) throws BusinessException {
        checkIfIdExist(id);

        CarMaintenanceDto carMaintenanceDto = this.modelMapperService.forDto().map(this.carMaintenanceDao.getById(id), CarMaintenanceDto.class);
        this.carMaintenanceDao.deleteById(id);
        return new SuccessDataResult(carMaintenanceDto, "Data Deleted");
    }

    @Override
    public List<CarMaintenanceListDto> getAllCarMaintenancesByCarId(int carId) throws BusinessException {

        List<CarMaintenance> carMaintenances = this.carMaintenanceDao.getByCar_CarId(carId);

        if (carMaintenances.isEmpty()) {
            return null;
        }
        List<CarMaintenanceListDto> carMaintenanceListDtos = carMaintenances.stream()
                .map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
                .collect(Collectors.toList());

        return carMaintenanceListDtos;
    }


    private void updateCarMaintenanceOperations(CarMaintenance carMaintenance, UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
        carMaintenance.setCarMaintenanceDescription(updateCarMaintenanceRequest.getCarMaintenanceDescription());
        carMaintenance.setReturnDate(updateCarMaintenanceRequest.getReturnDate());
    }

    private void checkIfCarExist(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {
        DataResult<CarDto> carDtoDataResult = this.carService.getById(createCarMaintenanceRequest.getCarCarId());

        if (carDtoDataResult == null) {
            throw new BusinessException("There is no car with following id: " + createCarMaintenanceRequest.getCarCarId());
        }
    }

    private void checkIfCarIsRental(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {
        List<RentalCarListDto> rentalCarListDtos = this.rentalCarService.getAllRentalCarsByCarId(createCarMaintenanceRequest.getCarCarId());

        if (rentalCarListDtos != null) {
            if (rentalCarListDtos.isEmpty()) {
                throw new BusinessException("There is no car rent operation with following id: " + createCarMaintenanceRequest.getCarCarId());
            }

            for (RentalCarListDto rentalCarListDto : rentalCarListDtos) {
                if (rentalCarListDto.getReturnDate() == null) {
                    throw new BusinessException("Car is in maintenance and return date is not estimated");
                }

                if (
                        createCarMaintenanceRequest.getReturnDate().isEqual(rentalCarListDto.getRentDate()) ||
                                createCarMaintenanceRequest.getReturnDate().isAfter(rentalCarListDto.getRentDate()) ||
                                createCarMaintenanceRequest.getReturnDate().isEqual(rentalCarListDto.getReturnDate()) ||
                                createCarMaintenanceRequest.getReturnDate().isBefore(rentalCarListDto.getReturnDate())
                ) {
                    throw new BusinessException("Maintenance is not possible! This car is rented from " + rentalCarListDto.getRentDate() + " to " + rentalCarListDto.getReturnDate());
                }
            }
        }
    }

    private void checkIfIdExist(int id) throws BusinessException {
        if (!this.carMaintenanceDao.existsById(id)) {
            throw new BusinessException("There is no car maintenance with following id" + id);
        }
    }

    private void checkIfUpdateParametersNotEqual(CarMaintenance carMaintenance, UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {
        if (
                carMaintenance.getCarMaintenanceDescription().equals(updateCarMaintenanceRequest.getCarMaintenanceDescription()) &&
                        carMaintenance.getReturnDate().isEqual(updateCarMaintenanceRequest.getReturnDate())
        ) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
        }
    }

    private void checkIfCarMaintenanceListEmpty(List<CarMaintenance> carMaintenances) throws BusinessException {
        if (carMaintenances.isEmpty()) {
            throw new BusinessException("There is no Car Maintenance to list");
        }
    }
}



