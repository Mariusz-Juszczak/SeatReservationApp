package com.seatreservation.service.mapper;

import com.seatreservation.domain.Restrictions;
import com.seatreservation.service.dto.RestrictionsDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestrictionsMapper extends EntityMapper<RestrictionsDto, Restrictions> {}
