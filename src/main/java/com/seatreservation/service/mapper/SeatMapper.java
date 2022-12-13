package com.seatreservation.service.mapper;

import com.seatreservation.domain.*;
import com.seatreservation.domain.enumeration.AvailabilityStatus;
import com.seatreservation.domain.enumeration.SeatStatus;
import com.seatreservation.service.dto.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SeatMapper extends EntityMapper<SeatDto, Seat> {
    @Mapping(target = "coordinates", source = "coordinates", qualifiedByName = "toDtoCoordinates")
    @Mapping(target = "dimensions", source = "dimensions", qualifiedByName = "toDtoDimensions")
    @Mapping(target = "room", source = "room", qualifiedByName = "toDtoRoom")
    @Mapping(target = "availabilityStatus" , expression = "java(calculateAvailabilityStatus(seat))")
    @Mapping(target = "equipments", source = "seat.equipments", qualifiedByName = "equipments")
    SeatDto toDto(Seat seat);

    @Named("toDtoCoordinates")
    CoordinatesDto toDtoCoordinates(Coordinates coordinates);

    @Named("toDtoDimensions")
    DimensionsDto toDtoDimensions(Dimensions dimensions);

    @Named("toDtoRoom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RoomDto toDtoRoom(Room room);

    @Named("equipments")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    EquipmentDto toDtoEquipment(Equipment equipments);

    default AvailabilityStatus calculateAvailabilityStatus(Seat seat){
        return seat.getStatus() == SeatStatus.FREE ? AvailabilityStatus.FREE : AvailabilityStatus.UNAVAILABLE;
    }
}
