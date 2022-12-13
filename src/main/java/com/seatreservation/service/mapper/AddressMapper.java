package com.seatreservation.service.mapper;

import com.seatreservation.domain.Address;
import com.seatreservation.service.dto.AddressDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDto, Address> {}
