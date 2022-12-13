package com.seatreservation.service.mapper;

import com.seatreservation.domain.Dimensions;
import com.seatreservation.service.dto.DimensionsDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DimensionsMapper extends EntityMapper<DimensionsDto, Dimensions> {}
