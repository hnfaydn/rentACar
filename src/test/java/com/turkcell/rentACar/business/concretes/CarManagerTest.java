package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarDamageService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorDto;
import com.turkcell.rentACar.business.requests.carRequests.CreateCarRequest;
import com.turkcell.rentACar.core.utilities.businessException.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperManager;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.entities.concretes.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarManagerTest {

    CarManager carManager;
    @Mock
    CarDao carDao;
    @Mock
    BrandService brandService;
    @Mock
    ColorService colorService;

    @Mock
    CarDamageService carDamageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        carManager = new CarManager(this.carDao,this.brandService,this.colorService,new ModelMapperManager(new ModelMapper()),carDamageService);
    }

    @Test
    void add() throws BusinessException {

        BrandDto brandDto= new BrandDto();
        ColorDto colorDto = new ColorDto();
        CreateCarRequest createCarRequest = new CreateCarRequest(10.0, 1950, "test", 10.0, 1, 1, null);

        given(brandService.getById(1)).willReturn(new SuccessDataResult(brandDto,"test" ));
        given(colorService.getById(1)).willReturn(new SuccessDataResult(colorDto,"test" ));

        boolean actual  = carManager.add(createCarRequest).isSuccess();

        Assertions.assertTrue(actual);

    }
}