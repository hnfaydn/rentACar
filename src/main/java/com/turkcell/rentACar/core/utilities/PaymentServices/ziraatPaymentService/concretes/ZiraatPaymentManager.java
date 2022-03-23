package com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.concretes;

import com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.abstracts.ZiraatPaymentService;
import com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.testDao.ZiraatTestDao;
import com.turkcell.rentACar.core.utilities.results.ErrorResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZiraatPaymentManager implements ZiraatPaymentService {

    private ZiraatTestDao ziraatTestDao;

    @Autowired
    public ZiraatPaymentManager(ZiraatTestDao ziraatTestDao) {
        this.ziraatTestDao = ziraatTestDao;
    }

    @Override
    public Result paymentResult(String cardNo, String month, String year, String cvv) {

        if (this.ziraatTestDao.existsZiraatTestEntityByCardNoAndMonthAndYearAndCvv(cardNo,month,year,cvv)){
            return new SuccessResult();
        }
        return new ErrorResult();
    }
}
