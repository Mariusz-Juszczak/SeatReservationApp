package com.seatreservation.service.mapper;

import com.seatreservation.domain.Location;
import com.seatreservation.domain.User;
import com.seatreservation.service.dto.LocationDto;
import com.seatreservation.service.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDto, Location> {

    @Named(value = "toDto")
    @Mapping(target = "locationAdmins", source = "locationAdmins", qualifiedByName = "userIdSet")
    LocationDto toDto(Location s);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDto toBasicDto(Location s);

    @IterableMapping(qualifiedByName = "toDto")
    List<LocationDto> toDto(List<Location> locations);

    @Mapping(target = "removeLocationAdmins", ignore = true)
    Location toEntity(LocationDto locationDto);


    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserDto toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDto> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
