package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACar.entities.concretes.Payment;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PaymentManager implements PaymentService {

    private PaymentDao paymentDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService) {
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll() throws BusinessException {
        return null;
    }

    @Override
    public Result add(RentalCar rentalCar,CreatePaymentRequest createPaymentRequest) throws BusinessException {

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest,Payment.class);

        payment.setPaymentDate(Date.from(Instant.now()));
        payment.setPaymentId(0);

        this.paymentDao.save(payment);

        return new SuccessResult();
    }

    @Override
    public DataResult<PaymentDto> getById(int id) throws BusinessException {
        return null;
    }

    @Override
    public Result update(int id, UpdatePaymentRequest updatePaymentRequest) throws BusinessException {
        return null;
    }

    @Override
    public Result delete(int id) throws BusinessException {
        return null;
    }
}
