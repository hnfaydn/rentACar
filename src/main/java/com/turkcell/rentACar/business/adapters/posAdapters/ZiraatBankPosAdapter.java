package com.turkcell.rentACar.business.adapters.posAdapters;

import com.turkcell.rentACar.business.abstracts.PosService;
import com.turkcell.rentACar.business.outServices.ZiraatBankPosManager;
import com.turkcell.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.core.utilities.results.ErrorResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import org.springframework.stereotype.Service;

@Service
public class ZiraatBankPosAdapter implements PosService {

    @Override
    public Result makePayment(double paymentAmount, CreatePaymentRequest createPaymentRequest) {

        ZiraatBankPosManager ziraatBankPosManager = new ZiraatBankPosManager();

        boolean makePaymentResult =
                ziraatBankPosManager.makePayment(
                        paymentAmount,
                        createPaymentRequest.getCardNo(),
                        createPaymentRequest.getCardHolder(),
                        createPaymentRequest.getExpirationMonth(),
                        createPaymentRequest.getExpirationYear(),
                        createPaymentRequest.getCvv());

        if (makePaymentResult){
            return new SuccessResult();
        }
        return new ErrorResult();
    }
}
