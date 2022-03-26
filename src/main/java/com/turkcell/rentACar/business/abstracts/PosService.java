package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface PosService {

    Result makePayment(double paymentAmount, CreatePaymentRequest createPaymentRequest);
}
