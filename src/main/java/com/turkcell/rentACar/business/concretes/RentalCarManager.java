package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.models.UpdatedRentalCarAndInvoiceDto;
import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
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
import com.turkcell.rentACar.entities.concretes.AdditionalService;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalCarManager implements RentalCarService {

    private final RentalCarDao rentalCarDao;
    private final CarMaintenanceService carMaintenanceService;
    private final CarService carService;
    private final ModelMapperService modelMapperService;
    private CustomerService customerService;
    private CityService cityService;
    private InvoiceService invoiceService;
    private AdditionalServiceService additionalServiceService;


    @Autowired
    public RentalCarManager(RentalCarDao rentalCarDao,
                            @Lazy CarMaintenanceService carMaintenanceService,
                            ModelMapperService modelMapperService,
                            CarService carService,
                            CityService cityService,
                            @Lazy CustomerService customerService,
                            @Lazy InvoiceService invoiceService,
                            @Lazy AdditionalServiceService additionalServiceService

    ) {
        this.rentalCarDao = rentalCarDao;
        this.carMaintenanceService = carMaintenanceService;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
        this.cityService = cityService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.additionalServiceService = additionalServiceService;

    }

    @Override
    public DataResult<List<RentalCarListDto>> getAll() throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAll();

        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream()
                .map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(rentalCarListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public SuccessDataResult add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);

        rentalCarInformationCheckOperations(rentalCar, createRentalCarRequest);

        this.carService.carKilometerSetOperation(rentalCar.getCar().getCarId(), rentalCar.getReturnKilometer());
        rentalCar.setCustomer(this.customerService.getCustomerById(createRentalCarRequest.getCustomerId()));

        rentalCar.setRentalCarId(0);
        this.rentalCarDao.save(rentalCar);

        return new SuccessDataResult(rentalCar, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public Result prePaymentControlOfRentalCar(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);

        checkIfAdditionalServiceListIsNullOrEmpty(rentalCar, createRentalCarRequest);
        rentalCarInformationCheckOperations(rentalCar, createRentalCarRequest);

        return new SuccessResult();
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getOrderedAdditionalServicesByRentalCarId(int rentalCarId) {

        RentalCar rentalCar = this.rentalCarDao.getById(rentalCarId);

        List<AdditionalService> additionalServices = rentalCar.getAdditionalServices();

        List<AdditionalServiceListDto> additionalServiceListDtos = additionalServices.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(additionalServiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public DataResult<RentalCarDto> getById(int id) throws BusinessException {

        checkIfIdExists(id);

        RentalCar rentalCar = this.rentalCarDao.getById(id);
        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);

        return new SuccessDataResult(rentalCarDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(int id, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        checkIfIdExists(id);

        RentalCar rentalCar = this.rentalCarDao.getById(id);

        LocalDate rentDate = rentalCar.getRentDate();

        checkIfCarIsExists(rentalCar.getCar().getCarId());
        checkIfUpdateParametersNotEqual(rentalCar, updateRentalCarRequest);
        checkIfRentalUpdateDatesCorrect(updateRentalCarRequest);
        checkIfRentDateAndMaintenanceUpdateDateValid(rentalCar, updateRentalCarRequest);
        checkIfUpdateCitiesAreCorrect(updateRentalCarRequest);

        rentalCarUpdateOperations(rentalCar, updateRentalCarRequest);

        this.carService.carKilometerSetOperation(updateRentalCarRequest.getCarCarId(), updateRentalCarRequest.getReturnKilometer());

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(rentalCar, RentalCarDto.class);

        rentalCar.setRentalCarId(id);
        this.rentalCarDao.save(rentalCar);

        rentalCarDto.setRentDate(rentDate);
        rentalCar.setRentDate(rentDate);
        InvoiceDto invoiceDto = this.invoiceService.reGenerateInvoiceForUpdatedRentalCar(rentalCar).getData();

        UpdatedRentalCarAndInvoiceDto updatedRentalCarAndInvoiceDto = new UpdatedRentalCarAndInvoiceDto();
        updatedRentalCarAndInvoiceDto.setUpdatedRentalCar(rentalCarDto);
        updatedRentalCarAndInvoiceDto.setUpdatedInvoice(invoiceDto);

        return new SuccessDataResult(updatedRentalCarAndInvoiceDto, BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfIdExists(id);

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(this.rentalCarDao.getById(id), RentalCarDto.class);

        this.rentalCarDao.deleteById(id);

        return new SuccessDataResult(rentalCarDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public List<RentalCarListDto> getAllRentalCarsByCarId(int carId) throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.getByCar_CarId(carId);

        if (rentalCars.isEmpty()) {
            return null;
        }

        List<RentalCarListDto> rentalCarListDtos = rentalCars.stream()
                .map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
                .collect(Collectors.toList());

        return rentalCarListDtos;
    }

    @Override
    public List<RentalCar> getAllRentalCars() {

        return this.rentalCarDao.findAll();
    }

    @Override
    public RentalCar getRentalCarById(int id) {

        if (this.rentalCarDao.getById(id) == null) {
            return null;
        }

        return this.rentalCarDao.getById(id);
    }

    @Override
    public void setDelayedReturnDate(int rentalCarId, LocalDate delayedReturnDate) {

        RentalCar rentalCar = this.rentalCarDao.getById(rentalCarId);
        rentalCar.setReturnDate(delayedReturnDate);

        this.rentalCarDao.save(rentalCar);
    }

    private void rentalCarInformationCheckOperations(RentalCar rentalCar, CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        checkIfCarIsExists(createRentalCarRequest.getCarCarId());
        checkIfRentalDatesCorrect(createRentalCarRequest);
        checkIfRentalCarCityIdsExists(createRentalCarRequest.getRentCityId(), createRentalCarRequest.getReturnCityId());
        checkIfCustomerExists(createRentalCarRequest);
        checkIfCarInMaintenance(createRentalCarRequest);
        checkIfCarUnderRental(createRentalCarRequest);
        checkIfAdditionalServiceListIsNullOrEmpty(rentalCar, createRentalCarRequest);

        rentalCar.setRentStartKilometer(this.carService.getById(createRentalCarRequest.getCarCarId()).getData().getKilometerInformation());

        checkIfKilometerInformationsValid(rentalCar);
    }

    private void checkIfCarInMaintenance(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> carMaintenanceListDtos =
                this.carMaintenanceService.getAllCarMaintenancesByCarId(createRentalCarRequest.getCarCarId());

        if (carMaintenanceListDtos != null) {
            if (carMaintenanceListDtos.isEmpty()) {
                throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_MAINTENANCE_NOT_FOUND + createRentalCarRequest.getCarCarId());
            }

            for (CarMaintenanceListDto carMaintenanceListDto : carMaintenanceListDtos) {
                if (carMaintenanceListDto.getReturnDate() == null) {
                    throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_IS_UNDER_MAINTENANCE_UNKNOWN_RETURN_DATE);
                }

                if (createRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getRentDate().isEqual(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getReturnDate().isBefore(carMaintenanceListDto.getReturnDate()) ||
                        createRentalCarRequest.getReturnDate().isEqual(carMaintenanceListDto.getReturnDate())) {
                    throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_IS_UNDER_MAINTENANCE_UNTIL + carMaintenanceListDto.getReturnDate());
                }
            }
        }
    }

    private void checkIfCarIsExists(int carId) throws BusinessException {

        DataResult<CarDto> carDtoDataResult = this.carService.getById(carId);

        if (carDtoDataResult == null) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_NOT_FOUND + carId);
        }
    }

    private void checkIfRentalDatesCorrect(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if (createRentalCarRequest.getReturnDate() != null) {
            if (createRentalCarRequest.getReturnDate().isBefore(createRentalCarRequest.getRentDate())) {
                throw new BusinessException(BusinessMessages.RentalCarMessages.RETURN_DATE_CANNOT_BEFORE_RENT_DAY);
            }
        }
    }

    private void checkIfIdExists(int id) throws BusinessException {

        if (!this.rentalCarDao.existsById(id)) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.RENTAL_CAR_NOT_FOUND + id);
        }
    }

    private void checkIfUpdateParametersNotEqual(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        List<AdditionalService> additionalServices = new ArrayList<>();

        for (Integer ids : updateRentalCarRequest.getAdditionalServiceIds()) {
            additionalServices.add(this.additionalServiceService.getAdditionalServiceById(ids));
        }

        if (updateRentalCarRequest.getRentDate().isEqual(rentalCar.getRentDate()) &&
                updateRentalCarRequest.getReturnDate().isEqual(rentalCar.getReturnDate()) &&
                updateRentalCarRequest.getCarCarId() == rentalCar.getRentalCarId() &&
                updateRentalCarRequest.getCustomerId() == rentalCar.getCustomer().getUserId() &&
                updateRentalCarRequest.getRentCityId() == rentalCar.getRentCity().getCityId() &&
                updateRentalCarRequest.getReturnCityId() == rentalCar.getReturnCity().getCityId() &&
                updateRentalCarRequest.getReturnKilometer() == rentalCar.getReturnKilometer() &&
                additionalServices.equals(rentalCar.getAdditionalServices())

        ) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.NO_CHANGES_NO_NEED_TO_UPDATE);
        }
    }

    private void checkIfCarUnderRental(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        List<RentalCar> rentalCars = this.rentalCarDao.findAllByCar_CarId(createRentalCarRequest.getCarCarId());

        if (!rentalCars.isEmpty()) {

            for (RentalCar rentalCar : rentalCars) {
                if (rentalCar.getReturnDate() == null) {
                    throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_IS_RENTED_UNKNOWN_RETURN_DATE);
                }
            }

            List<RentalCar> sortedRentalCars =
                    rentalCars.stream().sorted(Comparator.comparing(RentalCar::getReturnDate).reversed()).collect(Collectors.toList());

            if (createRentalCarRequest.getRentDate().isBefore(sortedRentalCars.get(0).getReturnDate())) {
                throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_IS_RENTED_RETURN_DATE_KNOWN +
                        sortedRentalCars.get(0).getRentDate() + BusinessMessages.TO + sortedRentalCars.get(0).getReturnDate());
            }
        }
    }

    private void checkIfRentDateAndMaintenanceUpdateDateValid(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> carMaintenanceListDtos = this.carMaintenanceService.getAllCarMaintenancesByCarId(rentalCar.getCar().getCarId());

        if (carMaintenanceListDtos != null) {

            for (CarMaintenanceListDto carMaintenanceListDto : carMaintenanceListDtos) {
                if (carMaintenanceListDto.getReturnDate() == null) {
                    throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_IS_UNDER_MAINTENANCE_UNKNOWN_RETURN_DATE);
                }

                if (updateRentalCarRequest.getRentDate().isBefore(carMaintenanceListDto.getReturnDate()) &&
                        updateRentalCarRequest.getReturnDate().isAfter(carMaintenanceListDto.getReturnDate())) {
                    throw new BusinessException(BusinessMessages.RentalCarMessages.CAR_UNDER_MAINTENANCE_RENT_NOT_POSSIBLE_UNTIL + carMaintenanceListDto.getReturnDate());
                }
            }
        }
    }

    private void checkIfRentalUpdateDatesCorrect(UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if (updateRentalCarRequest.getReturnDate() != null) {
            if (updateRentalCarRequest.getReturnDate().isBefore(updateRentalCarRequest.getRentDate())) {
                throw new BusinessException(BusinessMessages.RentalCarMessages.RETURN_DATE_CANNOT_BEFORE_RENT_DAY);
            }
        }
    }

    private void checkIfRentalCarCityIdsExists(int rentCity, int returnCity) throws BusinessException {

        if (!this.cityService.cityExistsById(rentCity)) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CITY_NOT_FOUND + rentCity);
        }

        if (!this.cityService.cityExistsById(returnCity)) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CITY_NOT_FOUND + returnCity);
        }
    }

    private void checkIfCustomerExists(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if (this.customerService.getById(createRentalCarRequest.getCustomerId()).getData() == null) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CUSTOMER_NOT_FOUND + createRentalCarRequest.getCustomerId());
        }
    }

    private void checkIfKilometerInformationsValid(RentalCar rentalCar) throws BusinessException {

        if (rentalCar.getRentStartKilometer() > rentalCar.getReturnKilometer()) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.RETURN_KILOMETER_NOT_VALID_FOR_CAR + rentalCar.getCar().getCarId());
        }
    }

    private void checkIfAdditionalServiceListIsNullOrEmpty(RentalCar rentalCar, CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if (createRentalCarRequest.getAdditionalServiceIds() == null || createRentalCarRequest.getAdditionalServiceIds().isEmpty()) {
            rentalCar.setAdditionalServices(null);
        } else {
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

        if (this.additionalServiceService.getAdditionalServiceById(additionalServiceId) == null) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.ADDITIONAL_SERVICE_NOT_FOUND + additionalServiceId);
        }
    }

    private void checkIfUpdateCitiesAreCorrect(UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if (!this.cityService.cityExistsById(updateRentalCarRequest.getRentCityId())) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CITY_NOT_FOUND + updateRentalCarRequest.getRentCityId());
        }
        if (!this.cityService.cityExistsById(updateRentalCarRequest.getReturnCityId())) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.CITY_NOT_FOUND + updateRentalCarRequest.getReturnCityId());
        }
    }

    private void rentalCarUpdateOperations(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        rentalCar.setRentDate(updateRentalCarRequest.getRentDate());
        rentalCar.setReturnDate(updateRentalCarRequest.getReturnDate());
        rentalCar.setRentCity(this.cityService.getCityById(updateRentalCarRequest.getRentCityId()));
        rentalCar.setReturnCity(this.cityService.getCityById(updateRentalCarRequest.getReturnCityId()));
        rentalCar.setRentStartKilometer(this.carService.getById(updateRentalCarRequest.getCarCarId()).getData().getKilometerInformation());
        rentalCar.setReturnKilometer(updateRentalCarRequest.getReturnKilometer());
        rentalCar.getCustomer().setUserId(updateRentalCarRequest.getCustomerId());

        if (rentalCar.getRentStartKilometer() > rentalCar.getReturnKilometer()) {
            throw new BusinessException(BusinessMessages.RentalCarMessages.RENT_AND_RETURN_KILOMETER_NOT_VALID);
        }
        updateRentalCarAdditionalServices(rentalCar, updateRentalCarRequest);
    }

    private void updateRentalCarAdditionalServices(RentalCar rentalCar, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        if (updateRentalCarRequest.getAdditionalServiceIds() == null || updateRentalCarRequest.getAdditionalServiceIds().isEmpty()) {
            rentalCar.setAdditionalServices(null);
        } else {
            List<AdditionalService> tempAdditionalServiceList = new ArrayList<>();

            for (Integer additionalServiceId : updateRentalCarRequest.getAdditionalServiceIds()) {

                checkIfAdditionalServiceIdExists(additionalServiceId);

                AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
                tempAdditionalServiceList.add(additionalService);
            }
            rentalCar.setAdditionalServices(tempAdditionalServiceList);
        }
    }
}
