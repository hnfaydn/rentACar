package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.InvoiceDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceManager implements InvoiceService {

    private InvoiceDao invoiceDao;
    private ModelMapperService modelMapperService;
    private RentalCarService rentalCarService;
    private AdditionalServiceService additionalServiceService;
    private CarService carService;

    @Autowired
    public InvoiceManager(InvoiceDao invoiceDao,
                          ModelMapperService modelMapperService,
                          RentalCarService rentalCarService,
                          AdditionalServiceService additionalServiceService,
                          CarService carService) {
        this.invoiceDao = invoiceDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
        this.additionalServiceService = additionalServiceService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAll() throws BusinessException {

        List<InvoiceListDto> invoiceListDtos =
                this.invoiceDao.findAll().stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(invoiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public SuccessDataResult add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException {

        checkIfRentalCarIdExists(createInvoiceRequest.getRentalCarId());

        Invoice invoice = this.modelMapperService.forDto().map(createInvoiceRequest,Invoice.class);
        RentalCarDto rentalCarDto = this.rentalCarService.getById(createInvoiceRequest.getRentalCarId()).getData();

        CarDto carDto = this.carService.getById(rentalCarDto.getCarDto().getCarId()).getData();

        rentalCarDto.setCarDto(carDto);

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumberCreator(createInvoiceRequest.getRentalCarId()));
        invoice.setAdditionalServiceTotalPayment(invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto));
        invoice.setRentDay(invoiceRentDayCalculations(rentalCarDto));
        invoice.setRentPayment(invoiceRentPaymentCalculations(rentalCarDto));
        invoice.setRentLocationPayment(invoiceRentLocationPaymentCalculations(rentalCarDto));
        invoice.setTotalPayment(invoice.getAdditionalServiceTotalPayment()+invoice.getRentPayment()+invoice.getRentLocationPayment());
        invoice.getCustomer().setUserId(rentalCarDto.getCustomerDto().getCustomerId());

        invoice.setInvoiceId(0);
        this.invoiceDao.save(invoice);

        return new SuccessDataResult(invoice, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<InvoiceDto> getById(int id) throws BusinessException {

        checkIfInvoiceIdExists(id);

        InvoiceDto invoiceDto = this.modelMapperService.forDto()
                .map(this.invoiceDao.getById(id),InvoiceDto.class);

        return new SuccessDataResult(invoiceDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfInvoiceIdExists(id);

        InvoiceDto invoiceDto = this.modelMapperService.forDto()
                .map(this.invoiceDao.getById(id),InvoiceDto.class);

        this.invoiceDao.deleteById(id);

        return new SuccessDataResult(invoiceDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllInvoicesByRentalCarId(int id) {

        List<Invoice> invoices = this.invoiceDao.findInvoicesByRentalCar_RentalCarId(id);

        List<InvoiceListDto> invoiceListDtos = invoices.stream()
                .map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(invoiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public double preInvoiceCalculator(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        RentalCarDto rentalCarDto = this.modelMapperService.forDto().map(createRentalCarRequest,RentalCarDto.class);

        checkIfAdditionalServiceListIsNullOrEmpty(rentalCarDto,createRentalCarRequest);

        Double preAdditionalServiceTotalPrice = invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto);
        Double preRentDayPrice = invoiceRentPaymentCalculations(rentalCarDto);
        Double preRentLocationPaymentPrice = invoiceRentLocationPaymentCalculations(rentalCarDto);
        return (preAdditionalServiceTotalPrice+preRentDayPrice+preRentLocationPaymentPrice);
    }

    @Override
    public DataResult<Invoice> generateDelayedRentalCarInvoice(RentalCar rentalCar) throws BusinessException {

        checkIfRentalCarIdExists(rentalCar.getRentalCarId());

        Invoice invoice = new Invoice();
        invoice.setRentalCar(rentalCar);

        RentalCarDto rentalCarDto = this.rentalCarService.getById(rentalCar.getRentalCarId()).getData();

        CarDto carDto = this.carService.getById(rentalCarDto.getCarDto().getCarId()).getData();

        rentalCarDto.setCarDto(carDto);

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumberCreator(rentalCar.getRentalCarId()));
        invoice.setAdditionalServiceTotalPayment(invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto));
        invoice.setRentDay(invoiceRentDayCalculations(rentalCarDto));
        invoice.setRentPayment(invoiceRentPaymentCalculations(rentalCarDto));
        invoice.setRentLocationPayment(invoiceRentLocationPaymentCalculations(rentalCarDto));
        invoice.setTotalPayment(invoice.getAdditionalServiceTotalPayment()+invoice.getRentPayment()+invoice.getRentLocationPayment());
        invoice.setCustomer(rentalCar.getCustomer());

        invoice.setInvoiceId(0);
        this.invoiceDao.save(invoice);

        return new SuccessDataResult(invoice, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public DataResult<InvoiceDto> reGenerateInvoiceForUpdatedRentalCar(RentalCar rentalCar) throws BusinessException {

        checkIfInvoiceNumberExists(rentalCar);

        Invoice invoice = this.invoiceDao.findInvoiceByInvoiceNumber(invoiceNumberCreator(rentalCar.getRentalCarId()));

        invoice.setRentalCar(rentalCar);

        RentalCarDto rentalCarDto = this.rentalCarService.getById(rentalCar.getRentalCarId()).getData();

        CarDto carDto = this.carService.getById(rentalCarDto.getCarDto().getCarId()).getData();

        rentalCarDto.setCarDto(carDto);

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumberCreator(rentalCar.getRentalCarId()));
        invoice.setAdditionalServiceTotalPayment(invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto));
        invoice.setRentDay(invoiceRentDayCalculations(rentalCarDto));
        invoice.setRentPayment(invoiceRentPaymentCalculations(rentalCarDto));
        invoice.setRentLocationPayment(invoiceRentLocationPaymentCalculations(rentalCarDto));
        invoice.setTotalPayment(invoice.getAdditionalServiceTotalPayment()+invoice.getRentPayment()+invoice.getRentLocationPayment());
        invoice.setCustomer(rentalCar.getCustomer());

        this.invoiceDao.save(invoice);

        return new SuccessDataResult(this.modelMapperService.forDto().map(invoice,InvoiceDto.class),BusinessMessages.GlobalMessages.DATA_UPDATED_TO_NEW_DATA);
    }

    private void checkIfInvoiceNumberExists(RentalCar rentalCar) throws BusinessException {
        if(this.invoiceDao.findInvoiceByInvoiceNumber(invoiceNumberCreator(rentalCar.getRentalCarId()))==null){
            throw new BusinessException(BusinessMessages.InvoiceMessages.INVOICE_NUMBER_NOT_FOUND+invoiceNumberCreator(rentalCar.getRentalCarId()));
        }
    }

    private void checkIfRentalCarIdExists(int rentalCarId) throws BusinessException {

        if(!this.rentalCarService.getById(rentalCarId).isSuccess()){
            throw new BusinessException(BusinessMessages.InvoiceMessages.RENTAL_CAR_NOT_FOUND+rentalCarId);
        }
    }

    private void checkIfInvoiceIdExists(int id) throws BusinessException {

        if(!this.invoiceDao.existsById(id)){
            throw new BusinessException(BusinessMessages.InvoiceMessages.INVOICE_NOT_FOUND+id);
        }
    }

    private int invoiceRentDayCalculations(RentalCarDto rentalCarDto) {

        if (ChronoUnit.DAYS.between(rentalCarDto.getRentDate(),rentalCarDto.getReturnDate())==0){
            return 1;
        }

        return  Integer.valueOf((int) ChronoUnit.DAYS.between(rentalCarDto.getRentDate(),rentalCarDto.getReturnDate()));
    }

    private double invoiceRentLocationPaymentCalculations(RentalCarDto rentalCarDto) {
        double rentLocationPayment = 0;

        if(rentalCarDto.getRentCityId()!=rentalCarDto.getReturnCityId()){
            return Double.valueOf(750);
        }

        return rentLocationPayment;
    }

    private double invoiceRentPaymentCalculations(RentalCarDto rentalCarDto) {

        Double totalPrice = invoiceRentDayCalculations(rentalCarDto) * rentalCarDto.getCarDto().getDailyPrice();

        return Double.valueOf((double) totalPrice);
    }

    private double invoiceAdditionalServiceTotalPaymentCalculator(RentalCarDto rentalCarDto) {

        double additionalServicesTotalPayment = 0;

        if(rentalCarDto.getAdditionalServices()==null||
                rentalCarDto.getAdditionalServices().isEmpty()){

            return additionalServicesTotalPayment;
        }
        else{

        for (AdditionalServiceDto additionalServiceDto : rentalCarDto.getAdditionalServices()
        ) {
            additionalServicesTotalPayment = additionalServicesTotalPayment + additionalServiceDto.getAdditionalServiceDailyPrice();
        }

        return (additionalServicesTotalPayment*invoiceRentDayCalculations(rentalCarDto));
        }
    }

    private Integer invoiceNumberCreator(int rentalCarId) throws BusinessException {

        RentalCarDto rentalCarDto = this.rentalCarService.getById(rentalCarId).getData();

        String invoiceNumber = String.valueOf(rentalCarDto.getCustomerDto().getCustomerId())+
                String.valueOf(rentalCarId)+
                String.valueOf(rentalCarDto.getRentDate().getYear()) +
                String.valueOf(rentalCarDto.getRentDate().getMonthValue()) +
                String.valueOf(rentalCarDto.getRentDate().getDayOfMonth());

        return Integer.valueOf(invoiceNumber);
    }

    private void checkIfAdditionalServiceListIsNullOrEmpty(RentalCarDto rentalCarDto, CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if(createRentalCarRequest.getAdditionalServiceIds()==null || createRentalCarRequest.getAdditionalServiceIds().isEmpty())
        {
            rentalCarDto.setAdditionalServices(null);
        }else{
            List<AdditionalServiceDto> tempAdditionalServiceList = new ArrayList<>();

            for (Integer additionalServiceId : createRentalCarRequest.getAdditionalServiceIds()) {

                checkIfAdditionalServiceIdExists(additionalServiceId);

                AdditionalServiceDto additionalServiceDto = this.additionalServiceService.getById(additionalServiceId).getData();
                tempAdditionalServiceList.add(additionalServiceDto);
            }
            rentalCarDto.setAdditionalServices(tempAdditionalServiceList);
        }
    }

    private void checkIfAdditionalServiceIdExists(Integer additionalServiceId) throws BusinessException {

        if(this.additionalServiceService.getAdditionalServiceById(additionalServiceId)==null){
            throw new BusinessException(BusinessMessages.RentalCarMessages.ADDITIONAL_SERVICE_NOT_FOUND+additionalServiceId);
        }
    }
}
