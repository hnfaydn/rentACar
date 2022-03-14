package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.AdditionalService;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderedAdditionalServiceManager implements OrderedAdditionalServiceService {

    private OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private ModelMapperService modelMapperService;
    private AdditionalServiceService additionalServiceService;

    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao,
                                           ModelMapperService modelMapperService,
                                           AdditionalServiceService additionalServiceService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAll() throws BusinessException {

        List<OrderedAdditionalService> orderedAdditionalServices = this.orderedAdditionalServiceDao.findAll();

        List<OrderedAdditionalServiceListDto> orderedAdditionalServiceListDtos = orderedAdditionalServices.stream()
                .map(orderedAdditionalService -> this.modelMapperService.forDto()
                        .map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult(orderedAdditionalServiceListDtos, "Data listed");
    }

    @Override
    public Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {

        List<AdditionalService> tempAdditionalServices = new ArrayList<>();

        for (Integer additionalServiceId : createOrderedAdditionalServiceRequest.getAdditionalServices()) {
            AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
            tempAdditionalServices.add(additionalService);
        }

        OrderedAdditionalService orderedAdditionalService =
                this.modelMapperService.forRequest().map(createOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        orderedAdditionalService.setAdditionalServices(tempAdditionalServices);

        OrderedAdditionalServiceDto orderedAdditionalServiceDto =
                this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceDto.class);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Orders added to following id: " + orderedAdditionalService.getOrderedAdditionalServiceId());
    }

    @Override
    public DataResult<OrderedAdditionalServiceDto> getById(int id) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getById(id);

        OrderedAdditionalServiceDto orderedAdditionalServiceDto =
                this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceDto.class);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data getted");
    }

    @Override
    public Result update(int id, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalRequest) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getById(id);

        List<AdditionalService> tempAdditionalServices = new ArrayList<>();

        for (Integer additionalServiceId : updateOrderedAdditionalRequest.getAdditionalServices()) {
            AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
            tempAdditionalServices.add(additionalService);
        }

        orderedAdditionalService.setAdditionalServices(tempAdditionalServices);

        OrderedAdditionalServiceDto orderedAdditionalServiceDto =
                this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceDto.class);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data updated, new data: ");
    }

    @Override
    public Result delete(int id) throws BusinessException {

        OrderedAdditionalServiceDto orderedAdditionalServiceDto =
                this.modelMapperService.forDto().map(this.orderedAdditionalServiceDao.getById(id), OrderedAdditionalServiceDto.class);

        this.orderedAdditionalServiceDao.deleteById(id);

        return new SuccessDataResult(orderedAdditionalServiceDto, "Data deleted");
    }

    @Override
    public OrderedAdditionalService updateRentalCarOrderedAdditionalService(int id, List<Integer> additionalServices) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getById(id);

        List<AdditionalService> tempAdditionalServices = new ArrayList<>();

        for (Integer additionalServiceId : additionalServices) {
            AdditionalService additionalService = this.additionalServiceService.getAdditionalServiceById(additionalServiceId);
            tempAdditionalServices.add(additionalService);
        }

        orderedAdditionalService.setAdditionalServices(tempAdditionalServices);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);
        return orderedAdditionalService;
    }

}
