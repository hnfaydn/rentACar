package com.turkcell.rentACar.core.utilities.businessException;

import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(validationErrors, "Validation Error: ");

        return errorDataResult;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleBusinessExceptions(BusinessException businessException) {
        String businessError = businessException.getMessage();
        ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(businessError, "Business Error");

        return errorDataResult;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleHttpMessageNotReadableExceptions(HttpMessageNotReadableException httpMessageNotReadableException) {
        String businessError = "JSON message format error";
        ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(businessError, "HttpMessageNotReadableException Error");

        return errorDataResult;
    }
}
