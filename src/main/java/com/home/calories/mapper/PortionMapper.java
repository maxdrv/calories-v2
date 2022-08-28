package com.home.calories.mapper;

import com.home.calories.model.Portion;
import com.home.calories.model.PortionInsert;
import com.home.calories.model.PortionUpdate;
import com.home.calories.openapi.model.CreatePortionDto;
import com.home.calories.openapi.model.PortionDto;
import com.home.calories.openapi.model.UpdatePortionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = BaseProductMapper.class
)
public interface PortionMapper {

    PortionDto map(Portion portion);

    PortionInsert map(Long dishId, CreatePortionDto dto);

    PortionUpdate map(Long dishId, Long portionId, UpdatePortionDto dto);

}
