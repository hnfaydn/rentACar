package com.turkcell.rentACar;


import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@RestControllerAdvice
public class RentACarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentACarApplication.class, args);
	}
	
	@Bean
	public ModelMapper getModelMapper(){
		return new ModelMapper();
	}


	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException){

		Map<String, String> validationErrors = new HashMap<>();

		for (FieldError fieldError: methodArgumentNotValidException.getBindingResult().getFieldErrors()) {

			validationErrors.put(fieldError.getField(),fieldError.getDefaultMessage());

		}

		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(validationErrors,"Validation Error: ");

		return errorDataResult;
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleBusinessExceptions(BusinessException businessException){
		String businessError = businessException.getMessage();

		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(businessError,"Business Error");

		return errorDataResult;
	}

}
