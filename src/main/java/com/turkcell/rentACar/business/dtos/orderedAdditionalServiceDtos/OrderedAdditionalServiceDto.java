package com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos;

import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalServiceDto {


    private List<AdditionalServiceDto> additionalServices;
}
