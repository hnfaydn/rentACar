package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityDto;
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
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.dataAccess.abstracts.RentalCarDao;
import com.turkcell.rentACar.entities.concretes.City;
import com.turkcell.rentACar.entities.concretes.Customer;
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
    private OrderedAdditionalServiceService orderedAdditionalServiceService;
    private CustomerService customerService;
    private CityService cityService;
    private InvoiceService invoiceService;

    @Autowired
    public RentalCarManager(RentalCarDao rentalCarDao,
                            @Lazy CarMaintenanceService carMaintenanceService,
                            OrderedAdditionalServiceService orderedAdditionalServiceService,
                            ModelMapperService modelMapperService, CarService carService,
                            CityService cityService,
                            @Lazy CustomerService customerService,
                            @Lazy InvoiceService invoiceService
    ) {
        this.rentalCarDao = rentalCarDao;
        this.carMaintenanceService = carMaintenanceService;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.cityService = cityService;
        this.customerService=customerService;
        this.invoiceService=invoiceService;
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAll() throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAll();

        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream()
                .map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(rentalCarListDtos, "Data Listed");
    }

    @Override
    public Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        checkIfCarIsExists(createRentalCarRequest.getCarCarId());
        checkIfRentalDatesCorrect(createRentalCarRequest);
        checkIfRentalCarCityIdsExists(createRentalCarRequest.getRentCityId(),createRentalCarRequest.getReturnCityId());
        checkIfCustomerExists(createRentalCarRequest);
        checkIfOrderedAdditionalServiceIdNotValidOrZero(createRentalCarRequest);
        checkIfCarInMaintenance(createRentalCarRequest);
        checkIfCarUnderRental(createRentalCarRequest);

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);

        rentalCar.setCustomer(this.customerService.getCustomerById(createRentalCarRequest.getCustomerId()));
        rentalCar.setRentStartKilometer(this.carService.getById(createRentalCarRequest.getCarCarId()).getData().getKilometerInformation());

        checkIfKilometerInformationsValid(rentalCar);

        this.carService.carKilometerSetOperation(rentalCar.getCar().getCarId(),rentalCar.getReturnKilometer());

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

        checkIfCarIsExists(rentalCar.getCar().getCarId());
        checkIfUpdateParametersNotEqual(rentalCar, updateRentalCarRequest);
        checkIfRentalUpdateDatesCorrect(updateRentalCarRequest);
        checkIfRentDateAndMaintenanceUpdateDateValid(rentalCar, updateRentalCarRequest);
        checkIfUpdateParametersOrderedAdditionalServiceIdNotValidOrZero(updateRentalCarRequest);

        updateRentalCarOperations(rentalCar, updateRentalCarRequest);

        rentalCar.setRentStartKilometer(this.carService.getById(updateRentalCarRequest.getCarCarId()).getData().getKilometerInformation());

        checkIfKilometerInformationsValid(rentalCar);

        this.carService.carKilometerSetOperation(rentalCar.getCar().getCarId(),rentalCar.getReturnKilometer());

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);
        rentalCar.setRentalCarId(rentalCar.getRentalCarId());

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

        List<RentalCarListDto> rentalCarListDtos =
                rentalCars.stream().map(rentalCar -> this.modelMapperService.forDto()
                        .map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return rentalCarListDtos;
    }

    @Override
    public void rentalCarOrderedAdditionalServiceIdSetOperation(int orderedAdditionalServiceId) {

        if(this.rentalCarDao.findRentalCarByOrderedAdditionalService_OrderedAdditionalServiceId(orderedAdditionalServiceId)!=null){
            this.rentalCarDao.findRentalCarByOrderedAdditionalService_OrderedAdditionalServiceId(orderedAdditionalServiceId).setOrderedAdditionalService(null);
        }
    }

    private void checkIfCarInMaintenance(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> carMaintenanceListDtos =
                this.carMaintenanceService.getAllCarMaintenancesByCarId(createRentalCarRequest.getCarCarId());

        if (carMaintenanceListDtos != null) {
            if (carMaintenanceListDtos.isEmpty()) {
                throw new BusinessException("There is no car maintenance with following id: " + createRentalCarRequest.getCarCarId());
            }

            for (CarMaintenanceListDto carMaintenanceListDto : carMaintenanceListDtos) {
                if (carMaintenanceListDto.getReturnDate() == null) {
                    throw new BusinessException("Car is under maintenance and return date is not estimated");
                }

                if (createRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getRentDate().isEqual(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getReturnDate().isBefore(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getReturnDate().isEqual(carMaintenanceListDto.getReturnDate())) {
                    throw new BusinessException("This car is under maintenance until:" + carMaintenanceListDto.getReturnDate());
                }
            }
        }
    }

    private void checkIfCarIsExists(int carId) throws BusinessException {

        DataResult<CarDto> carDtoDataResult = this.carService.getById(carId);

        if (carDtoDataResult == null) {
            throw new BusinessException("There is no car with following id: " + carId);
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
            throw new BusinessException("There is no rental car with following id: " + id);
        }
    }

    private void checkIfUpdateParametersNotEqual(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if (updateRentalCarRequest.getRentDate().isEqual(rentalCar.getRentDate()) && updateRentalCarRequest.getReturnDate().isEqual(rentalCar.getReturnDate())) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
        }
    }

    private void updateRentalCarOperations(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        rentalCar.setRentDate(updateRentalCarRequest.getRentDate());
        rentalCar.setReturnDate(updateRentalCarRequest.getReturnDate());
        if(rentalCar.getOrderedAdditionalService()!=null){
        rentalCar.getOrderedAdditionalService().setOrderedAdditionalServiceId(updateRentalCarRequest.getOrderedAdditionalServiceId());
        }
    }

    private void checkIfCarUnderRental(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAllByCar_CarId(createRentalCarRequest.getCarCarId());

        if (!rentalCars.isEmpty()) {

            for (RentalCar rentalCar : rentalCars) {
                if (rentalCar.getReturnDate() == null) {
                    throw new BusinessException("This car is still under rental and return date unestimated");
                }
            }

            List<RentalCar> sortedRentalCars =
                    rentalCars.stream().sorted(Comparator.comparing(RentalCar::getReturnDate).reversed()).collect(Collectors.toList());

            if (createRentalCarRequest.getRentDate().isBefore(sortedRentalCars.get(0).getReturnDate())) {
                throw new BusinessException("This car is under rental from " + sortedRentalCars.get(0).getRentDate() + " to " + sortedRentalCars.get(0).getReturnDate());
            }
        }
    }

    private void checkIfRentDateAndMaintenanceUpdateDateValid(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> carMaintenanceListDtos = this.carMaintenanceService.getAllCarMaintenancesByCarId(rentalCar.getCar().getCarId());

        if (carMaintenanceListDtos != null) {

            for (CarMaintenanceListDto carMaintenanceListDto : carMaintenanceListDtos) {
                if (carMaintenanceListDto.getReturnDate() == null) {
                    throw new BusinessException("Car is under maintenance and return date is not estimated");
                }

                if (updateRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) &&
                        updateRentalCarRequest.getReturnDate().isAfter(carMaintenanceListDto.getReturnDate())) {
                    throw new BusinessException("Rental update is not possible! This car is under maintenance until: " + carMaintenanceListDto.getReturnDate());
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

    private void checkIfRentalCarCityIdsExists(int rentCity,int returnCity) throws BusinessException {

        if(!this.cityService.cityExistsById(rentCity)){
            throw new BusinessException("There is no rent city with following id: " +rentCity);
        }

        if(!this.cityService.cityExistsById(returnCity)){
            throw new BusinessException("There is no return city with following id: " +returnCity);
        }
    }

    private void checkIfOrderedAdditionalServiceIdNotValidOrZero(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if (createRentalCarRequest.getOrderedAdditionalServiceId()<0){
            throw new BusinessException("Ordered Additional Service can not less than zero");
        }

        if(createRentalCarRequest.getOrderedAdditionalServiceId()==0){
            createRentalCarRequest.setOrderedAdditionalServiceId(null);
        }
    }

    private void checkIfUpdateParametersOrderedAdditionalServiceIdNotValidOrZero(UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if (updateRentalCarRequest.getOrderedAdditionalServiceId()<0){
            throw new BusinessException("Ordered Additional Service can not less than zero");
        }

        if(updateRentalCarRequest.getOrderedAdditionalServiceId()==0){
            updateRentalCarRequest.setOrderedAdditionalServiceId(null);
        }
    }

    private void checkIfCustomerExists(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        if(this.customerService.getCustomerById(createRentalCarRequest.getCustomerId())==null){
            throw new BusinessException("There is no customer with following id: "+createRentalCarRequest.getCustomerId());
        }
    }

    private void checkIfKilometerInformationsValid(RentalCar rentalCar) throws BusinessException {

        if(rentalCar.getRentStartKilometer() > rentalCar.getReturnKilometer()){
            throw new BusinessException("Return kilometer is not valid for following car id: "+rentalCar.getCar().getCarId());
        }
    }
}
