package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.api.models.CreatePaymentModel;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface PaymentService {

    DataResult<List<PaymentListDto>> getAll();
    Result add(CreatePaymentModel createPaymentModel) throws BusinessException;
    void paymentSuccessor(CreatePaymentModel createPaymentModel) throws BusinessException;
    Result delete(int id);
    DataResult<PaymentDto> getById(int id);
}
