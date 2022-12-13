package com.seatreservation.service.mapper;

import com.seatreservation.domain.Coordinates;
import com.seatreservation.domain.Dimensions;
import com.seatreservation.domain.Floor;
import com.seatreservation.domain.Room;
import com.seatreservation.service.dto.CoordinatesDto;
import com.seatreservation.service.dto.DimensionsDto;
import com.seatreservation.service.dto.FloorDto;
import com.seatreservation.service.dto.RoomDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDto, Room> {
    @Mapping(target = "coordinates", source = "coordinates", qualifiedByName = "toDtoCoordinates")
    @Mapping(target = "dimensions", source = "dimensions", qualifiedByName = "toDtoDimensions")
    @Mapping(target = "floor", source = "floor", qualifiedByName = "floorId")
    RoomDto toDto(Room s);

    @Named("toDtoCoordinates")
    CoordinatesDto toDtoCoordinates(Coordinates coordinates);

    @Named("toDtoDimensions")
    DimensionsDto toDtoDimensions(Dimensions dimensions);

    @Named("floorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FloorDto toDtoFloorId(Floor floor);
}
