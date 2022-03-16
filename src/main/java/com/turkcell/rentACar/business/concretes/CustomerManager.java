package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManager implements CustomerService {

    private CustomerDao customerDao;
    private ModelMapperService modelMapperService;
    private RentalCarService rentalCarService;

    @Autowired
    public CustomerManager(CustomerDao customerDao,
                           ModelMapperService modelMapperService,
                           @Lazy RentalCarService rentalCarService) {
        this.customerDao = customerDao;
        this.modelMapperService = modelMapperService;
        this.rentalCarService=rentalCarService;
    }

    @Override
    public DataResult<List<CustomerListDto>> getAll() {
        List<Customer> customers = this.customerDao.findAll();

        List<CustomerListDto> customerListDtos = customers.stream()
                .map(customer -> this.modelMapperService.forDto().map(customer, CustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(customerListDtos,"Data listed successfully");
    }

    @Override
    public DataResult<CustomerDto> getById(int id) throws BusinessException {

        checkIfCustomerIdExists(id);

        Customer customer = this.customerDao.getById(id);

        CustomerDto customerDto = this.modelMapperService.forDto().map(customer, CustomerDto.class);

        return new SuccessDataResult(customerDto,"Data Brought Successfully:");
    }



    @Override
    public Result update(int id, UpdateCustomerRequest updateCustomerRequest) throws BusinessException {

        checkIfCustomerIdExists(id);
        checkIfCustomerEmailAlreadyExists(updateCustomerRequest.getEmail());

        Customer customer = this.customerDao.getById(id);

        customerUpdateOperations(customer,updateCustomerRequest);

        CustomerDto customerDto = this.modelMapperService.forDto().map(customer,CustomerDto.class);

        return new SuccessDataResult(customerDto,"Customer email and password updated successfully.");
    }

    private void customerUpdateOperations(Customer customer, UpdateCustomerRequest updateCustomerRequest) {
        customer.setEmail(updateCustomerRequest.getEmail());
        customer.setPassword(updateCustomerRequest.getPassword());
    }

    private void checkIfCustomerEmailAlreadyExists(String email) throws BusinessException {
        if (this.customerDao.existsCustomerByEmail(email)){
            throw new BusinessException("This email is already using by another customer: "+email);
        }
    }

    @Override
    public Result delete(int id) throws BusinessException {

        checkIfCustomerIdExists(id);

        CustomerDto customerDto = this.modelMapperService.forDto().map(this.customerDao.getById(id), CustomerDto.class);

        this.customerDao.deleteById(id);

        return new SuccessDataResult(customerDto,"Data Deleted: ");
    }

    @Override
    public Customer getCustomerById(int id) {
        if (this.customerDao.getById(id)==null){
            return null;
        }
        return this.customerDao.getById(id);
    }

    private void checkIfCustomerIdExists(int id) throws BusinessException {
        if(!this.customerDao.existsById(id)){
            throw new BusinessException("There is no customer with following id: "+id);
        }
    }
}
