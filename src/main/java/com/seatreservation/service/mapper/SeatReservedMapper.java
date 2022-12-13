package com.seatreservation.service.mapper;

import com.seatreservation.domain.SeatReserved;
import com.seatreservation.domain.User;
import com.seatreservation.service.dto.SeatReservedDto;
import com.seatreservation.service.dto.UserDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SeatReservedMapper extends EntityMapper<SeatReservedDto, SeatReserved> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "seatId", source = "seat.id")
    SeatReservedDto toDto(SeatReserved s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDto toDtoUserId(User user);

    @Mapping(target = "seat.id", source = "seatId")
    SeatReserved toEntity(SeatReservedDto dto);
}
