package com.seatreservation.service.mapper;

import com.seatreservation.domain.Equipment;
import com.seatreservation.domain.Seat;
import com.seatreservation.service.dto.EquipmentDto;
import com.seatreservation.service.dto.SeatDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EquipmentMapper extends EntityMapper<EquipmentDto, Equipment> {
    @Mapping(target = "seat", source = "seat", qualifiedByName = "seatId")
    EquipmentDto toDto(Equipment s);

    @Named("seatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SeatDto toDtoSeatId(Seat seat);
}
