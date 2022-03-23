package com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.testDao;

import com.turkcell.rentACar.core.utilities.PaymentServices.ziraatPaymentService.testEntities.ZiraatTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZiraatTestDao extends JpaRepository<ZiraatTestEntity, Integer> {

    boolean existsZiraatTestEntityByCardNoAndMonthAndYearAndCvv(String cardNo, String month, String year, String cvv);
}
