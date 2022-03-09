package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.RentalCarDao;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class RentalCarManager implements RentalCarService {

    private final RentalCarDao rentalCarDao;
    private final CarMaintenanceService carMaintenanceService;
    private final CarService carService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public RentalCarManager(RentalCarDao rentalCarDao,@Lazy CarMaintenanceService carMaintenanceService, ModelMapperService modelMapperService, CarService carService) {
        this.rentalCarDao = rentalCarDao;
        this.carMaintenanceService = carMaintenanceService;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAll() throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAll();


        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(rentalCarListDtos, "Data Listed");
    }

    @Override
    public Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        checkIfCarExists(createRentalCarRequest);
        checkIfRentalDatesCorrect(createRentalCarRequest);
        checkIfCarInMaintenance(createRentalCarRequest);
        checkIfCarUnderRental(createRentalCarRequest);

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);
        rentalCar.setRentalCarId(0);
        this.rentalCarDao.save(rentalCar);
        return new SuccessDataResult(createRentalCarRequest, "Data added");
    }


    @Override
    public DataResult<RentalCarDto> getById(int id) throws BusinessException {
        checkIfIdExists(id);
        RentalCar rentalCar = this.rentalCarDao.getById(id);

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);

        return new SuccessDataResult(rentalCarDto, "Data getted by following id: " + id);
    }

    @Override
    public Result update(int id, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        checkIfIdExists(id);
        RentalCar rentalCar = this.rentalCarDao.getById(id);

        checkIfUpdateParametersNotEqual(rentalCar, updateRentalCarRequest);
        checkIfRentalUpdateDatesCorrect(updateRentalCarRequest);
        checkIfRentDateAndMaintenanceUpdateDateValid(rentalCar,updateRentalCarRequest);
        updateRentalCarOperations(rentalCar, updateRentalCarRequest);

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);
        this.rentalCarDao.save(rentalCar);
        return new SuccessDataResult(rentalCarDto, "Data updated, new data: ");
    }



    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);
        this.rentalCarDao.deleteById(id);
        return new SuccessResult("Data Deleted");
    }

    @Override
    public List<RentalCarListDto> getAllRentalCarsByCarId(int carId) throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.getByCar_CarId(carId);

        if (rentalCars.isEmpty()) {
            return null;
        }
        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return rentalCarListDtos;
    }

    private void checkIfCarInMaintenance(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        List<CarMaintenanceListDto> carMaintenanceListDtos = this.carMaintenanceService.getAllCarMaintenancesByCarId(createRentalCarRequest.getCarCarId());

        if (carMaintenanceListDtos != null) {
            if (carMaintenanceListDtos.isEmpty()) {
                throw new BusinessException("There is no car maintenance with following id: " + createRentalCarRequest.getCarCarId());
            }

            for (CarMaintenanceListDto carMaintenanceListDto : carMaintenanceListDtos) {
                if (carMaintenanceListDto.getReturnDate() == null) {
                    throw new BusinessException("Car is under maintenance and return date is not estimated");
                }

                if (createRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) || createRentalCarRequest.getRentDate().isEqual(carMaintenanceListDto.getReturnDate()) || createRentalCarRequest.getReturnDate().isBefore(carMaintenanceListDto.getReturnDate()) || createRentalCarRequest.getReturnDate().isEqual(carMaintenanceListDto.getReturnDate())) {
                    throw new BusinessException("This car is under maintenance until:" + carMaintenanceListDto.getReturnDate());
                }
            }
        }
    }

    private void checkIfCarExists(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        DataResult<CarDto> carDtoDataResult = this.carService.getById(createRentalCarRequest.getCarCarId());

        if (carDtoDataResult == null) {
            throw new BusinessException("There is no car with following id: " + createRentalCarRequest.getCarCarId());
        }
    }

    private void checkIfRentalDatesCorrect(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        if (createRentalCarRequest.getReturnDate() != null) {
            if (createRentalCarRequest.getReturnDate().isBefore(createRentalCarRequest.getRentDate())) {
                throw new BusinessException("Return date can not before rent date");
            }
        }
    }

    private void checkIfIdExists(int id) throws BusinessException {
        if (!this.rentalCarDao.existsById(id)) {
            throw new BusinessException("There is no rental car with following id" + id);
        }
    }

    private void checkIfUpdateParametersNotEqual(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        if (updateRentalCarRequest.getRentDate().isEqual(rentalCar.getRentDate()) && updateRentalCarRequest.getReturnDate().isEqual(rentalCar.getReturnDate())) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
        }
    }

    private void updateRentalCarOperations(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) {
        rentalCar.setRentDate(updateRentalCarRequest.getRentDate());
        rentalCar.setReturnDate(updateRentalCarRequest.getReturnDate());
    }

    private void checkIfCarUnderRental(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        List<RentalCar> rentalCars = this.rentalCarDao.findAllByCar_CarId(createRentalCarRequest.getCarCarId());

        if(!rentalCars.isEmpty()) {

            for (RentalCar rentalCar:rentalCars) {
                if(rentalCar.getReturnDate()==null){
                    throw new BusinessException("This car is still under rental and return date unestimated");
                }
            }

            List<RentalCar> sortedRentalCars =
                    rentalCars.stream().sorted(Comparator.comparing(RentalCar::getReturnDate).reversed()).collect(Collectors.toList());

            if(createRentalCarRequest.getRentDate().isBefore(sortedRentalCars.get(0).getReturnDate())){
                throw new BusinessException("This car is under rental from "+sortedRentalCars.get(0).getRentDate()+" to "+sortedRentalCars.get(0).getReturnDate());
            }
        }
    }

    private void checkIfRentDateAndMaintenanceUpdateDateValid(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> carMaintenanceListDtos = this.carMaintenanceService.getAllCarMaintenancesByCarId(rentalCar.getCar().getCarId());

        if(carMaintenanceListDtos != null){

            for (CarMaintenanceListDto carMaintenanceListDto: carMaintenanceListDtos)
            {
                if(carMaintenanceListDto.getReturnDate()==null){
                    throw new BusinessException("Car is under maintenance and return date is not estimated");
                }

                if(updateRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) &&
                    updateRentalCarRequest.getReturnDate().isAfter(carMaintenanceListDto.getReturnDate())){
                    throw new BusinessException("Rental update is not possible! This car is under maintenance until:  " +carMaintenanceListDto.getReturnDate() );
                }
            }
        }

    }

    private void checkIfRentalUpdateDatesCorrect(UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        if (updateRentalCarRequest.getReturnDate() != null) {
            if (updateRentalCarRequest.getReturnDate().isBefore(updateRentalCarRequest.getRentDate())) {
                throw new BusinessException("Return date can not before rent date");
            }
        }
    }
}
