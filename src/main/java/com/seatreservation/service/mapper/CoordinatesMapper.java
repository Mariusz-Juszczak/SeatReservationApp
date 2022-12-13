package com.seatreservation.service.mapper;

import com.seatreservation.domain.Coordinates;
import com.seatreservation.service.dto.CoordinatesDto;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CoordinatesMapper extends EntityMapper<CoordinatesDto, Coordinates> {}
