package com.seatreservation.service.mapper;

import com.seatreservation.domain.Building;
import com.seatreservation.domain.Dimensions;
import com.seatreservation.domain.Floor;
import com.seatreservation.service.dto.BuildingDto;
import com.seatreservation.service.dto.DimensionsDto;
import com.seatreservation.service.dto.FloorDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FloorMapper extends EntityMapper<FloorDto, Floor> {
    @Mapping(target = "dimensions", source = "dimensions", qualifiedByName = "toDtoDimensionsId")
    @Mapping(target = "building", source = "building", qualifiedByName = "toDtoBuildingId")
    FloorDto toDto(Floor s);

    @Named("toDtoDimensionsId")
    DimensionsDto toDtoDimensionsId(Dimensions dimensions);

    @Named("toDtoBuildingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BuildingDto toDtoBuildingId(Building building);
}
