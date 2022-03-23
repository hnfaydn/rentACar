package com.turkcell.rentACar.core.utilities.adopters.paymentServiceAdopter.abstracts;

import com.turkcell.rentACar.core.utilities.results.Result;

public interface ZiraatPaymentAdopterService {

        Result payment(String cardNo, String month, String year, String cvv);
}
