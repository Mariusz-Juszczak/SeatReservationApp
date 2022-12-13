package com.seatreservation.service.mapper;

import com.seatreservation.domain.Address;
import com.seatreservation.domain.Building;
import com.seatreservation.domain.Location;
import com.seatreservation.service.dto.AddressDto;
import com.seatreservation.service.dto.BuildingDto;
import com.seatreservation.service.dto.LocationDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BuildingMapper extends EntityMapper<BuildingDto, Building> {
    @Mapping(target = "address", source = "address", qualifiedByName = "toDtoAddressId")
    @Mapping(target = "location", source = "location", qualifiedByName = "toDtoLocationId")
    BuildingDto toDto(Building s);

    @Named("toDtoAddressId")
    AddressDto toDtoAddressId(Address address);

    @Named("toDtoLocationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDto toDtoLocationId(Location location);
}


