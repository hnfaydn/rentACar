package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceDto;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarDto;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.InvoiceDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceManager implements InvoiceService {

    private InvoiceDao invoiceDao;
    private ModelMapperService modelMapperService;
    private RentalCarService rentalCarService;

    @Autowired
    public InvoiceManager(InvoiceDao invoiceDao,
                          ModelMapperService modelMapperService,
                          RentalCarService rentalCarService) {
        this.invoiceDao = invoiceDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAll() throws BusinessException {

        List<Invoice> invoices = this.invoiceDao.findAll();

        List<InvoiceListDto> invoiceListDtos =
                invoices.stream().map(invoice -> this.modelMapperService.forDto()
                        .map(invoice, InvoiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(invoiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public SuccessDataResult add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException {

        checkIfRentalCarIdExists(createInvoiceRequest.getRentalCarId());

        Invoice invoice = this.modelMapperService.forDto().map(createInvoiceRequest,Invoice.class);
        RentalCarDto rentalCarDto = this.rentalCarService.getById(createInvoiceRequest.getRentalCarId()).getData();

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumberCreator(invoice,createInvoiceRequest.getRentalCarId()));
        invoice.setAdditionalServiceTotalPayment(invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto));
        invoice.setRentDay(invoiceRentDayCalculations(rentalCarDto));
        invoice.setRentPayment(invoiceRentPaymentCalculations(rentalCarDto));
        invoice.setRentLocationPayment(invoiceRentLocationPaymentCalculations(rentalCarDto));
        invoice.setTotalPayment(invoice.getAdditionalServiceTotalPayment()+invoice.getRentPayment()+invoice.getRentLocationPayment());
        invoice.getCustomer().setUserId(rentalCarDto.getCustomerDto().getUserId());

        invoice.setInvoiceId(0);
        this.invoiceDao.saveAndFlush(invoice);

        return new SuccessDataResult(invoice, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
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

    private Double invoiceRentPaymentCalculations(RentalCarDto rentalCarDto) {

        if (ChronoUnit.DAYS.between(rentalCarDto.getRentDate(),rentalCarDto.getReturnDate())==0){
            return rentalCarDto.getCarDto().getDailyPrice();
        }

        return (ChronoUnit.DAYS.between(rentalCarDto.getRentDate(),rentalCarDto.getReturnDate()))*rentalCarDto.getCarDto().getDailyPrice();
    }

    private double invoiceAdditionalServiceTotalPaymentCalculator(RentalCarDto rentalCarDto) {

        double additionalServicesTotalPayment = 0;

        if(rentalCarDto.getAdditionalServices()==null||
           rentalCarDto.getAdditionalServices().isEmpty()){

            return additionalServicesTotalPayment;
        }

            for (AdditionalServiceDto additionalServiceDto : rentalCarDto.getAdditionalServices()
            ) {
                additionalServicesTotalPayment = additionalServicesTotalPayment+additionalServiceDto.getAdditionalServiceDailyPrice();
            }

            return additionalServicesTotalPayment;
    }

    private Integer invoiceNumberCreator(Invoice invoice, int rentalCarID) throws BusinessException {

        RentalCarDto rentalCarDto = this.rentalCarService.getById(rentalCarID).getData();

        String invoiceNumber = String.valueOf(rentalCarDto.getCustomerDto().getUserId())+
                String.valueOf(rentalCarID)+
                String.valueOf(invoice.getInvoiceDate().getYear()) +
                String.valueOf(invoice.getInvoiceDate().getMonthValue()) +
                String.valueOf(invoice.getInvoiceDate().getDayOfMonth());

        return Integer.valueOf(invoiceNumber);
    }

    @Override
    public DataResult<InvoiceDto> getById(int id) throws BusinessException {

        checkIfInvoiceIdExitst(id);

        Invoice invoice = this.invoiceDao.getById(id);

        InvoiceDto invoiceDto = this.modelMapperService.forDto().map(invoice,InvoiceDto.class);

        return new SuccessDataResult(invoiceDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    public Result reGenerateInvoiceByUsingRentalCarId(int id) throws BusinessException {

        checkIfInvoiceIdExitst(id);

        Invoice invoice = this.invoiceDao.getById(id);
        RentalCarDto rentalCarDto = this.rentalCarService.getById(invoice.getRentalCar().getRentalCarId()).getData();

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumberCreator(invoice,invoice.getRentalCar().getRentalCarId()));
        invoice.setAdditionalServiceTotalPayment(invoiceAdditionalServiceTotalPaymentCalculator(rentalCarDto));
        invoice.setRentDay(invoiceRentDayCalculations(rentalCarDto));
        invoice.setRentPayment(invoiceRentPaymentCalculations(rentalCarDto));
        invoice.setRentLocationPayment(invoiceRentLocationPaymentCalculations(rentalCarDto));
        invoice.setTotalPayment(invoice.getAdditionalServiceTotalPayment()+invoice.getRentPayment()+invoice.getRentLocationPayment());
        invoice.getRentalCar().setRentalCarId(invoice.getRentalCar().getRentalCarId());

        InvoiceDto invoiceDto = this.modelMapperService.forDto().map(invoice,InvoiceDto.class);

        this.invoiceDao.save(invoice);

        return new SuccessDataResult(invoiceDto, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfInvoiceIdExitst(id);

        InvoiceDto invoiceDto = this.modelMapperService.forDto().map(this.invoiceDao.getById(id),InvoiceDto.class);

        this.invoiceDao.deleteById(id);

        return new SuccessDataResult(invoiceDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<List<InvoiceListDto>> getAllInvoicesByRentalCarId(int id) throws BusinessException {

        List<Invoice> invoices = this.invoiceDao.findInvoicesByRentalCar_RentalCarId(id);

        List<InvoiceListDto> invoiceListDtos =
                invoices.stream().map(invoice -> this.modelMapperService.forDto()
                        .map(invoice, InvoiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(invoiceListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    private void checkIfRentalCarIdExists(int rentalCarId) throws BusinessException {
        if(!this.rentalCarService.getById(rentalCarId).isSuccess()){
            throw new BusinessException(BusinessMessages.InvoiceMessages.RENTAL_CAR_NOT_FOUND+rentalCarId);
        }
    }

    private void checkIfInvoiceIdExitst(int id) throws BusinessException {
        if(!this.invoiceDao.existsById(id)){
            throw new BusinessException(BusinessMessages.InvoiceMessages.INVOICE_NOT_FOUND+id);
        }
    }
}
