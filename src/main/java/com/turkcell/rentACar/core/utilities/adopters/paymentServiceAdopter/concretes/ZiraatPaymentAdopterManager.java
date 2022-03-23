package com.turkcell.rentACar.core.utilities.adopters.paymentServiceAdopter.concretes;

import com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.abstracts.ZiraatPaymentService;
import com.turkcell.rentACar.core.utilities.adopters.paymentServiceAdopter.abstracts.ZiraatPaymentAdopterService;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZiraatPaymentAdopterManager implements ZiraatPaymentAdopterService {

    private ZiraatPaymentService ziraatPaymentService;

    @Autowired
    public ZiraatPaymentAdopterManager(ZiraatPaymentService ziraatPaymentService) {
        this.ziraatPaymentService = ziraatPaymentService;
    }

    @Override
    public Result payment(String cardNo, String month, String year, String cvv) {

        return this.ziraatPaymentService.paymentResult(cardNo,month,year,cvv);
    }
}
