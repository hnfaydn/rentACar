package com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos;

import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalServiceListDto {

    private int orderedAdditionalServiceId;

    private List<AdditionalServiceListDto> additionalServices;
}
