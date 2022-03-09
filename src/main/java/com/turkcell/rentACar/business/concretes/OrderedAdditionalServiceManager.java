package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditinalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAddtionalRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderedAdditionalServiceManager implements OrderedAdditionalServiceService {

    private OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAll() throws BusinessException {

        List<OrderedAdditionalService> orderedAdditionalServices = this.orderedAdditionalServiceDao.findAll();

        List<OrderedAdditionalServiceListDto> orderedAdditionalServiceListDtos = orderedAdditionalServices.stream()
                .map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class))
                .collect(Collectors.toList());

        for (OrderedAdditionalServiceListDto orderedAdditionalServiceListDto:orderedAdditionalServiceListDtos
             ) {

            for (OrderedAdditionalService orderedAdditionalService:orderedAdditionalServices
                 ) {
                orderedAdditionalServiceListDto.setCustomerId(orderedAdditionalService.getCustomer().getCustomerId());
                orderedAdditionalServiceListDto.setRentalCarId(orderedAdditionalService.getRentalCar().getRentalCarId());
            }

        }


        return new SuccessDataResult<>(orderedAdditionalServiceListDtos, "Data listed");
    }

    @Override
    public Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalRequest) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(createOrderedAdditionalRequest, OrderedAdditionalService.class);


        this.orderedAdditionalServiceDao.save(orderedAdditionalService);

        return new SuccessDataResult(createOrderedAdditionalRequest, "Data added");
    }

    @Override
    public DataResult<OrderedAdditionalServiceDto> getById(int id) throws BusinessException {
        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getById(id);

        OrderedAdditionalServiceDto orderedAdditionalServiceDto = this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceDto.class);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data getted");
    }

    @Override
    public Result update(int id, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalRequest) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getById(id);

        orderedAdditionalService.setOrderedAdditionalServiceId(updateOrderedAdditionalRequest.getAdditionalServiceId());
        orderedAdditionalService.getRentalCar().setRentalCarId(updateOrderedAdditionalRequest.getRentalCarId());

        OrderedAdditionalServiceDto orderedAdditionalServiceDto = this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceDto.class);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data updated, new data: ");
    }

    @Override
    public Result delete(int id) throws BusinessException {

        OrderedAdditionalServiceDto orderedAdditionalServiceDto = this.modelMapperService.forDto().map(this.orderedAdditionalServiceDao.getById(id), OrderedAdditionalServiceDto.class);
        this.orderedAdditionalServiceDao.deleteById(id);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data deleted");
    }
}
