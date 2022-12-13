package com.seatreservation.service.mapper;

import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.service.dto.NotificationAlertDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationAlertMapper extends EntityMapper<NotificationAlertDto, NotificationAlert> {}
