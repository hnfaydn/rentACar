package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.api.models.CreateDelayedPaymentModel;
import com.turkcell.rentACar.api.models.CreatePaymentModel;
import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentDto;
import com.turkcell.rentACar.business.dtos.paymentDtos.PaymentListDto;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/paymentsController")
public class PaymentsController {

    private PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @GetMapping("/getAll")
    DataResult<List<PaymentListDto>> getAll(){
        return this.paymentService.getAll();
    }

    @PostMapping("/add")
    Result add(@RequestBody @Valid CreatePaymentModel createPaymentModel) throws BusinessException {
        return this.paymentService.add(createPaymentModel);
    }

    @DeleteMapping("/delete")
    Result delete(@RequestParam int id) throws BusinessException {
        return this.paymentService.delete(id);
    }

    @GetMapping("/getById")
    DataResult<PaymentDto> getById(@RequestParam int id) throws BusinessException {
        return this.paymentService.getById(id);
    }

    @PostMapping("/additionalPaymentForDelaying")
    Result additionalPaymentForDelaying(@RequestBody @Valid CreateDelayedPaymentModel createDelayedPaymentModel) throws BusinessException{
        return this.paymentService.additionalPaymentForDelaying(createDelayedPaymentModel);
    }

}
