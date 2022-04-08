package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.api.models.CreateDelayedPaymentModel;
import com.turkcell.rentACar.api.models.CreatePaymentModel;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface PaymentService {

    DataResult<List<PaymentListDto>> getAll();

    Result paymentForIndividualCustomer(CreatePaymentModel createPaymentModel) throws BusinessException;

    Result paymentForCorporateCustomer(CreatePaymentModel createPaymentModel) throws BusinessException;

    void paymentSuccessorForIndividualCustomer(CreatePaymentModel createPaymentModel) throws BusinessException;

    void paymentSuccessorForCorporateCustomer(CreatePaymentModel createPaymentModel) throws BusinessException;

    void delayedPaymentSuccessor(CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException;

    Result delete(int id) throws BusinessException;

    DataResult<PaymentDto> getById(int id) throws BusinessException;

    Result additionalPaymentForDelaying(CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException;
}
