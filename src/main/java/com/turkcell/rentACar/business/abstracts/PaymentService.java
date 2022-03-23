package com.turkcell.rentACar.business.abstracts;


import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.RentalCar;

import java.util.List;

public interface PaymentService {

    DataResult<List<PaymentListDto>> getAll() throws BusinessException;

    Result add(RentalCar rentalCar, CreatePaymentRequest createPaymentRequest) throws BusinessException;

    DataResult<PaymentDto> getById(int id) throws BusinessException;

    Result update(int id, UpdatePaymentRequest updatePaymentRequest) throws BusinessException;

    Result delete(int id) throws BusinessException;
}
