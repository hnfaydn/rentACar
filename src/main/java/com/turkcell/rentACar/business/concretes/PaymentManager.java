package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.models.CreatePaymentModel;
import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.abstracts.PosService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
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

    @Autowired
    public PaymentManager(PaymentDao paymentDao,
                          ModelMapperService modelMapperService,
                          RentalCarService rentalCarService,
                          InvoiceService invoiceService,
                          PosService posService) {
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
        this.invoiceService = invoiceService;
        this.posService = posService;
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll() {

        List<Payment> payments = this.paymentDao.findAll();

        List<PaymentListDto> paymentListDtos = payments.stream()
                .map(payment -> this.modelMapperService.forDto().map(payment, PaymentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(paymentListDtos, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Override
    public Result add(CreatePaymentModel createPaymentModel) throws BusinessException {

        paymentSuccessor(createPaymentModel);

        return new SuccessDataResult(createPaymentModel.getCreatePaymentRequest(), BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    public void paymentSuccessor(CreatePaymentModel createPaymentModel) throws BusinessException {

        RentalCar rentalCar = this.rentalCarService.add(createPaymentModel.getCreateRentalCarRequest()).getData();

        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
        createInvoiceRequest.setRentalCarId(rentalCar.getRentalCarId());
        Invoice invoice = this.invoiceService.add(createInvoiceRequest).getData();

        checkIfPaymentDone(invoice,createPaymentModel.getCreatePaymentRequest());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentModel.getCreatePaymentRequest(), Payment.class);
        payment.setCustomer(rentalCar.getCustomer());
        payment.setPaymentAmount(invoice.getTotalPayment());
        payment.setRentalCar(rentalCar);
        this.paymentDao.saveAndFlush(payment);
    }

    private void checkIfPaymentDone(Invoice invoice, CreatePaymentRequest createPaymentRequest) throws BusinessException {
        if (!posService.makePayment(invoice.getTotalPayment(),createPaymentRequest).isSuccess()){
                throw new BusinessException(BusinessMessages.PaymentMessages.INVALID_PAYMENT);
        }
    }


    @Override
    public Result delete(int id) {

        Payment payment = this.paymentDao.getById(id);

        PaymentDto paymentDto = this.modelMapperService.forRequest().map(payment, PaymentDto.class);

        this.paymentDao.deleteById(id);

        return new SuccessDataResult(paymentDto, BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY);
    }

    @Override
    public DataResult<PaymentDto> getById(int id) {

        Payment payment = this.paymentDao.getById(id);

        PaymentDto paymentDto = this.modelMapperService.forRequest().map(payment, PaymentDto.class);

        return new SuccessDataResult(paymentDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY);
    }
}
