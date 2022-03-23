package com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.abstracts;

import com.turkcell.rentACar.core.utilities.results.Result;

public interface ZiraatPaymentService {

    Result paymentResult(String cardNo, String month, String year, String cvv);
}
