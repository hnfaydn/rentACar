package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.adopters.paymentServiceAdopter.abstracts.ZiraatPaymentAdopterService;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.RentalCarDao;
import com.turkcell.rentACar.entities.concretes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalCarManager implements RentalCarService {

    private final RentalCarDao rentalCarDao;
    private final CarMaintenanceService carMaintenanceService;
    private final CarService carService;
    private final ModelMapperService modelMapperService;
    private CustomerService customerService;
    private CityService cityService;
    private InvoiceService invoiceService;
    private AdditionalServiceService additionalServiceService;
    private ZiraatPaymentAdopterService ziraatPaymentAdopterService;
    private PaymentService paymentService;

    @Autowired
    public RentalCarManager(RentalCarDao rentalCarDao,
                            @Lazy CarMaintenanceService carMaintenanceService,
                            ModelMapperService modelMapperService, CarService carService,
                            CityService cityService,
                            @Lazy CustomerService customerService,
                            @Lazy InvoiceService invoiceService,
                            @Lazy AdditionalServiceService additionalServiceService,
                            ZiraatPaymentAdopterService ziraatPaymentAdopterService,
                            PaymentService paymentService
    ) {
        this.rentalCarDao = rentalCarDao;
        this.carMaintenanceService = carMaintenanceService;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
        this.cityService = cityService;
        this.customerService=customerService;
        this.invoiceService=invoiceService;
        this.additionalServiceService=additionalServiceService;
        this.ziraatPaymentAdopterService=ziraatPaymentAdopterService;
        this.paymentService=paymentService;
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAll() throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAll();

        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream()
                .map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(rentalCarListDtos, "Data Listed");
    }

    @Override
    @Transactional
    public Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        checkIfCarIsExists(createRentalCarRequest.getCarCarId());
        checkIfRentalDatesCorrect(createRentalCarRequest);
        checkIfRentalCarCityIdsExists(createRentalCarRequest.getRentCityId(),createRentalCarRequest.getReturnCityId());
        checkIfCustomerExists(createRentalCarRequest);
        checkIfCarInMaintenance(createRentalCarRequest);
        checkIfCarUnderRental(createRentalCarRequest);

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);

        checkIfAdditionalServiceListIsNullOrEmpty(rentalCar,createRentalCarRequest);

        rentalCar.setCustomer(this.customerService.getCustomerById(createRentalCarRequest.getUserId()));
        rentalCar.setRentStartKilometer(this.carService.getById(createRentalCarRequest.getCarCarId()).getData().getKilometerInformation());

        checkIfKilometerInformationsValid(rentalCar);

        this.carService.carKilometerSetOperation(rentalCar.getCar().getCarId(),rentalCar.getReturnKilometer());

        checkIfPaymentIsSuccess(createRentalCarRequest.getPaymentInformations());

        paymentAddOperation(rentalCar,createRentalCarRequest.getPaymentInformations());

        rentalCar.setRentalCarId(0);
        this.rentalCarDao.save(rentalCar);

        return new SuccessDataResult(createRentalCarRequest, "Data added");
    }

    @Override
    @Transactional
    public void paymentAddOperation(RentalCar rentalCar, CreatePaymentRequest createPaymentRequest) throws BusinessException {
        this.paymentService.add(rentalCar,createPaymentRequest);
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getOrderedAdditionalServicesByRentalCarId(int rentalCarId) {
        RentalCar rentalCar = this.rentalCarDao.getById(rentalCarId);

        List<AdditionalService> additionalServices = rentalCar.getAdditionalServices();

        List<AdditionalServiceListDto> additionalServiceListDtos = additionalServices.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService,AdditionalServiceListDto.class)).collect(Collectors.toList());
        return new SuccessDataResult( additionalServiceListDtos,"Ordered Additional Services Listed:");
    }

    private void checkIfPaymentIsSuccess(CreatePaymentRequest paymentInformations) throws BusinessException {

        if(!this.ziraatPaymentAdopterService.payment(
                paymentInformations.getCardNo(), paymentInformations.getMonth(), paymentInformations.getYear(), paymentInformations.getCvv()).isSuccess())
        {
            throw new BusinessException("Invalid payment information");
        }
    }

    private void checkIfAdditionalServiceListIsNullOrEmpty(RentalCar rentalCar, CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if(createRentalCarRequest.getAdditionalServiceIds()==null || createRentalCarRequest.getAdditionalServiceIds().isEmpty())
        {
            rentalCar.setAdditionalServices(null);
        }else{
            List<AdditionalService> tempAdditionalServiceList = new ArrayList<>();

            for (Integer additionalServiceId : createRentalCarRequest.getAdditionalServiceIds()) {

                checkIfAdditionalServiceIdExists(additionalServiceId);

                AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
                tempAdditionalServiceList.add(additionalService);
            }
            rentalCar.setAdditionalServices(tempAdditionalServiceList);
        }
    }

    private void checkIfAdditionalServiceIdExists(Integer additionalServiceId) throws BusinessException {
        if(this.additionalServiceService.getAdditionalServiceById(additionalServiceId)==null){
            throw new BusinessException("There is no Additional service with following id: "+additionalServiceId);
        }
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
        checkIfUpdateCitiesAreCorrect(updateRentalCarRequest);

        rentalCarUpdateOperations(rentalCar,updateRentalCarRequest);

        this.carService.carKilometerSetOperation(updateRentalCarRequest.getCarCarId(),updateRentalCarRequest.getReturnKilometer());
        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);

        rentalCar.setRentalCarId(rentalCar.getRentalCarId());

        this.rentalCarDao.save(rentalCar);

        return new SuccessDataResult(rentalCarDto, "Data updated, new data: ");
    }

    private void checkIfUpdateCitiesAreCorrect(UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        if(!this.cityService.cityExistsById(updateRentalCarRequest.getRentCityId())){
            throw new BusinessException("There is no rent city with following id: "+updateRentalCarRequest.getRentCityId());
        }
        if(!this.cityService.cityExistsById(updateRentalCarRequest.getReturnCityId())){
            throw new BusinessException("There is no rent city with following id: "+updateRentalCarRequest.getReturnCityId());
        }
    }

    private void rentalCarUpdateOperations(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {
        rentalCar.setRentDate(updateRentalCarRequest.getRentDate());
        rentalCar.setRentDate(updateRentalCarRequest.getReturnDate());
        rentalCar.getRentCity().setCityId(updateRentalCarRequest.getRentCityId());
        rentalCar.getReturnCity().setCityId(updateRentalCarRequest.getReturnCityId());
        rentalCar.setRentStartKilometer(this.carService.getById(updateRentalCarRequest.getCarCarId()).getData().getKilometerInformation());
        rentalCar.setReturnKilometer(updateRentalCarRequest.getReturnKilometer());
        rentalCar.getCustomer().setUserId(updateRentalCarRequest.getUserId());

        if(rentalCar.getRentStartKilometer() > rentalCar.getReturnKilometer()){
            throw new BusinessException("Return kilometer can not before Rent kilometer.");
        }
        updateRentalCarAdditionalServices(rentalCar,updateRentalCarRequest);
    }

    private void updateRentalCarAdditionalServices(RentalCar rentalCar,UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if(updateRentalCarRequest.getAdditionalServiceIds()==null || updateRentalCarRequest.getAdditionalServiceIds().isEmpty())
        {
            rentalCar.setAdditionalServices(null);
        }else{
            List<AdditionalService> tempAdditionalServiceList = new ArrayList<>();

            for (Integer additionalServiceId : updateRentalCarRequest.getAdditionalServiceIds()) {

                checkIfAdditionalServiceIdExists(additionalServiceId);

                AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
                tempAdditionalServiceList.add(additionalService);
            }
            rentalCar.setAdditionalServices(tempAdditionalServiceList);
        }
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
    public List<RentalCar> getAllRentalCars() {
        return this.rentalCarDao.findAll();
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
        List<AdditionalService> additionalServices = new ArrayList<>();

        for (Integer ids : updateRentalCarRequest.getAdditionalServiceIds()){
            additionalServices.add(this.additionalServiceService.getAdditionalServiceById(ids));
        }

        if (    updateRentalCarRequest.getRentDate().isEqual(rentalCar.getRentDate()) &&
                updateRentalCarRequest.getReturnDate().isEqual(rentalCar.getReturnDate()) &&
                updateRentalCarRequest.getCarCarId()==rentalCar.getRentalCarId() &&
                updateRentalCarRequest.getUserId()==rentalCar.getCustomer().getUserId() &&
                updateRentalCarRequest.getRentCityId()==rentalCar.getRentCity().getCityId() &&
                updateRentalCarRequest.getReturnCityId()==rentalCar.getReturnCity().getCityId()&&
                updateRentalCarRequest.getReturnKilometer()==rentalCar.getReturnKilometer()&&
                additionalServices.equals(rentalCar.getAdditionalServices())

        ) {
            throw new BusinessException("Initial values are completely equal to update values, no need to update!");
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

    private void checkIfCustomerExists(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {
        if(this.customerService.getCustomerById(createRentalCarRequest.getUserId())==null){
            throw new BusinessException("There is no customer with following id: "+createRentalCarRequest.getUserId());
        }
    }

    private void checkIfKilometerInformationsValid(RentalCar rentalCar) throws BusinessException {

        if(rentalCar.getRentStartKilometer() > rentalCar.getReturnKilometer()){
            throw new BusinessException("Return kilometer is not valid for following car id: "+rentalCar.getCar().getCarId());
        }
    }
}
