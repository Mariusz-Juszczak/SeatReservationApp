package com.seatreservation.service.mapper;

import com.seatreservation.domain.Address;
import com.seatreservation.domain.Equipment;
import com.seatreservation.domain.SeatReserved;

import com.seatreservation.domain.User;
import com.seatreservation.service.dto.EquipmentDto;
import com.seatreservation.service.dto.SeatReservedDetailsDto;
import com.seatreservation.service.dto.UserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;;import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SeatReservedDetailsMapper extends EntityMapper<SeatReservedDetailsDto, SeatReserved> {
    @Mapping(target = "locationId", source = "seat.room.floor.building.location.id")
    @Mapping(target = "buildingId", source = "seat.room.floor.building.id")
    @Mapping(target = "floorId", source = "seat.room.floor.id")
    @Mapping(target = "roomId", source = "seat.room.id")
    @Mapping(target = "seatId", source = "seat.id")
    @Mapping(target = "locationName", source = "seat.room.floor.building.location.name")
    @Mapping(target = "buildingName", source = "seat.room.floor.building.name")
    @Mapping(target = "floorName", source = "seat.room.floor.name")
    @Mapping(target = "roomName", source = "seat.room.name")
    @Mapping(target = "seatName", source = "seat.name")
    @Mapping(target = "user", source = "user", qualifiedByName = "user")
    @Mapping(target = "buildingAddress", source = "seat.room.floor.building.address", qualifiedByName = "buildingAddress")
    @Mapping(target = "equipments", source = "seat.equipments", qualifiedByName = "equipments")
    SeatReservedDetailsDto toDto(SeatReserved s);

    @Named("buildingAddress")
    static String buildingAddress(Address address) {
        return address != null ? address.getCity() + ", " + address.getStreet() : "";
    }
    @Named("user")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserDto toDtoUserId(User user);

    @Named("equipments")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    EquipmentDto toDtoEquipment(Equipment equipments);
}
