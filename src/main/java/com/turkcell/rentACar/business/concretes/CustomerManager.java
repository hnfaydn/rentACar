package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerListDto;
import com.turkcell.rentACar.business.requests.customerRequests.UpdateCustomerRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CustomerDao;
import com.turkcell.rentACar.entities.concretes.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManager implements CustomerService {

    private CustomerDao customerDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CustomerManager(CustomerDao customerDao,
                           ModelMapperService modelMapperService) {
        this.customerDao = customerDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<CustomerListDto>> getAll() {
        List<Customer> customers = this.customerDao.findAll();

        List<CustomerListDto> customerListDtos = customers.stream()
                .map(customer -> this.modelMapperService.forDto().map(customer, CustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(customerListDtos,"Data listed successfully");
    }

    @Override
    public DataResult<CustomerDto> getById(int id) {

        Customer customer = this.customerDao.getById(id);

        CustomerDto customerDto = this.modelMapperService.forDto().map(customer, CustomerDto.class);

        return new SuccessDataResult(customerDto,"Data Brought Successfully:");
    }

    @Override
    public Result update(int id, UpdateCustomerRequest updateCustomerRequest) {

        Customer customer = this.customerDao.getById(id);

        customer.setEmail(updateCustomerRequest.getEmail());
        customer.setPassword(updateCustomerRequest.getPassword());

        CustomerDto customerDto = this.modelMapperService.forDto().map(customer,CustomerDto.class);

        return new SuccessDataResult(customerDto,"Customer email and password updated successfully.");
    }

    @Override
    public Result delete(int id) throws BusinessException {

        CustomerDto customerDto = this.modelMapperService.forDto().map(this.customerDao.getById(id), CustomerDto.class);

        this.customerDao.deleteById(id);

        return new SuccessDataResult(customerDto,"Data Deleted: ");
    }
}
