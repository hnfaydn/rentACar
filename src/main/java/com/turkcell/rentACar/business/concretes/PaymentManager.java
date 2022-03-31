package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.models.CreateDelayedPaymentModel;
import com.turkcell.rentACar.api.models.CreatePaymentModel;
import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import com.turkcell.rentACar.entities.concretes.Payment;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentManager implements PaymentService {

    private PaymentDao paymentDao;
    private ModelMapperService modelMapperService;
    private RentalCarService rentalCarService;
    private InvoiceService invoiceService;
    private PosService posService;
    private CarService carService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao,
                          ModelMapperService modelMapperService,
                          RentalCarService rentalCarService,
                          InvoiceService invoiceService,
                          PosService posService,
                          CarService carService) {
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
        this.invoiceService = invoiceService;
        this.posService = posService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll() {

        List<Payment> payments = this.paymentDao.findAll();

        List<PaymentListDto> paymentListDtos = payments.stream()
                .map(payment -> this.modelMapperService.forDto().map(payment, PaymentListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult(paymentListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public Result add(CreatePaymentModel createPaymentModel) throws BusinessException {

        rentalCarPaymentPreControlOperation(createPaymentModel.getCreateRentalCarRequest());
        checkIfPaymentDone(preInvoiceCalculator(createPaymentModel.getCreateRentalCarRequest()),createPaymentModel.getCreatePaymentRequest());
        paymentSuccessor(createPaymentModel);

        return new SuccessDataResult(createPaymentModel, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public void paymentSuccessor(CreatePaymentModel createPaymentModel) throws BusinessException {

        RentalCar rentalCar = this.rentalCarService.add(createPaymentModel.getCreateRentalCarRequest()).getData();

        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
        createInvoiceRequest.setRentalCarId(rentalCar.getRentalCarId());
        Invoice invoice = this.invoiceService.add(createInvoiceRequest).getData();

        Payment payment = this.modelMapperService.forRequest().map(createPaymentModel.getCreatePaymentRequest(), Payment.class);
        payment.setCustomer(rentalCar.getCustomer());
        payment.setPaymentAmount(invoice.getTotalPayment());
        payment.setRentalCar(rentalCar);

        this.paymentDao.save(payment);
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfPaymentIdExists(id);

        Payment payment = this.paymentDao.getById(id);

        PaymentDto paymentDto = this.modelMapperService.forRequest().map(payment, PaymentDto.class);

        this.paymentDao.deleteById(id);

        return new SuccessDataResult(paymentDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<PaymentDto> getById(int id) throws BusinessException {

        checkIfPaymentIdExists(id);

        Payment payment = this.paymentDao.getById(id);

        PaymentDto paymentDto = this.modelMapperService.forRequest().map(payment, PaymentDto.class);

        return new SuccessDataResult(paymentDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public Result additionalPaymentForDelaying(CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException {

        checkIfRentalCarIdExists(createDelayedPaymentModel.getRentalCarId());

        RentalCar rentalCar = this.rentalCarService.getRentalCarById(createDelayedPaymentModel.getRentalCarId());
        CreateRentalCarRequest createRentalCarRequest = this.modelMapperService.forRequest().map(rentalCar,CreateRentalCarRequest.class);

        checkIfDelayedDateIsCorrect(createRentalCarRequest,createDelayedPaymentModel);
        checkIfDelayedKilometerIsCorrect(createRentalCarRequest,createDelayedPaymentModel);

        rentalCar.setRentDate(rentalCar.getReturnDate());
        rentalCar.setReturnDate(createDelayedPaymentModel.getDelayedReturnDate());

        checkIfPaymentDone(preInvoiceCalculator(createRentalCarRequest),createDelayedPaymentModel.getCreatePaymentRequest());
        delayedPaymentSuccessor(createDelayedPaymentModel);

        PaymentDto paymentDto = this.modelMapperService.forDto().map(createDelayedPaymentModel, PaymentDto.class);

        return new SuccessDataResult(paymentDto, BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public void delayedPaymentSuccessor(CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException {

        RentalCar rentalCar = this.rentalCarService.getRentalCarById(createDelayedPaymentModel.getRentalCarId());

        this.rentalCarService.setDelayedReturnDate(createDelayedPaymentModel.getRentalCarId(),createDelayedPaymentModel.getDelayedReturnDate());
        this.carService.carKilometerSetOperation(rentalCar.getCar().getCarId(),createDelayedPaymentModel.getCarDelayedKilometerInformation());

        Invoice invoice = this.invoiceService.generateDelayedRentalCarInvoice(rentalCar).getData();

        Payment payment = this.modelMapperService.forRequest().map(createDelayedPaymentModel.getCreatePaymentRequest(), Payment.class);
        payment.setCustomer(rentalCar.getCustomer());
        payment.setPaymentAmount(invoice.getTotalPayment());
        payment.setRentalCar(rentalCar);

        paymentDao.save(payment);
    }

    private void rentalCarPaymentPreControlOperation(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        if(!this.rentalCarService.prePaymentControlOfRentalCar(createRentalCarRequest).isSuccess()){
            throw new BusinessException(BusinessMessages.PaymentMessages.INVALID_RENTAL_CAR_INFORMATION);
        }
    }

    private Double preInvoiceCalculator(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        return this.invoiceService.preInvoiceCalculator(createRentalCarRequest);
    }

    private void checkIfPaymentDone(Double paymentAmount, CreatePaymentRequest createPaymentRequest) throws BusinessException {

        if (!posService.makePayment(paymentAmount,createPaymentRequest).isSuccess()){
            throw new BusinessException(BusinessMessages.PaymentMessages.INVALID_PAYMENT);
        }
    }

    private void checkIfPaymentIdExists(int id) throws BusinessException {

        if(!this.paymentDao.existsById(id)){
            throw new BusinessException(BusinessMessages.PaymentMessages.PAYMENT_NOT_FOUND);
        }
    }

    private void checkIfRentalCarIdExists(int rentalCarId) throws BusinessException {

        if(this.rentalCarService.getRentalCarById(rentalCarId)==null){
            throw new BusinessException(BusinessMessages.PaymentMessages.RENTAL_CAR_NOT_FOUND+rentalCarId);
        }
    }

    private void checkIfDelayedKilometerIsCorrect(CreateRentalCarRequest createRentalCarRequest, CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException {

        if(createDelayedPaymentModel.getCarDelayedKilometerInformation()<createRentalCarRequest.getReturnKilometer()){
            throw new BusinessException(BusinessMessages.PaymentMessages.DELAYED_RETURN_KILOMETER_IS_NOT_VALID);
        }
    }

    private void checkIfDelayedDateIsCorrect(CreateRentalCarRequest createRentalCarRequest, CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException {

        if(createDelayedPaymentModel.getDelayedReturnDate().isBefore(createRentalCarRequest.getReturnDate())){
            throw new BusinessException(BusinessMessages.PaymentMessages.DELAYED_RETURN_DATE_IS_NOT_VALID);
        }
    }
}
