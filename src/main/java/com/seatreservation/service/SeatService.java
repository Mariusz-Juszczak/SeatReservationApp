package com.seatreservation.service;

import com.seatreservation.domain.enumeration.AvailabilityStatus;
import com.seatreservation.repository.SeatRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.seatreservation.service.dto.SeatDto;
import com.seatreservation.service.mapper.SeatMapper;
import com.seatreservation.web.rest.errors.BadRequestAlertException;
import com.seatreservation.domain.Floor;
import com.seatreservation.domain.Restrictions;
import com.seatreservation.domain.Seat;
import com.seatreservation.domain.SeatReserved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final ReservationService reservationService;
    private final RestrictionsService restrictionsService;
    private final RestrictionHandler restrictionHandler;

    public SeatService(SeatRepository seatRepository, SeatMapper seatMapper, ReservationService reservationService, RestrictionsService restrictionsService, RestrictionHandler restrictionHandler) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
        this.reservationService = reservationService;
        this.restrictionsService = restrictionsService;
        this.restrictionHandler = restrictionHandler;
    }

    public Seat save(Seat seat) {
        if (seat.getId() != null) {
            throw new BadRequestAlertException("A new seat cannot already have an ID", Seat.class, "id exists");
        }
        log.debug("Request to save Seat : {}", seat);
        return seatRepository.save(seat);
    }

    public Seat update(Seat seat,Long id) {
        if (seat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", Seat.class, "id null");
        }
        if (!Objects.equals(id, seat.getId())) {
            throw new BadRequestAlertException("Invalid ID", Seat.class, "id invalid");
        }

        if (!seatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", Seat.class, "id not found");
        }
        log.debug("Request to save Seat : {}", seat);
        return seatRepository.save(seat);
    }
    public List<Seat> findListOfSeatPerBuilding(Long buildingId){
        return seatRepository.findSeatsByBuildingId(buildingId);
    }

    //work in progress
    public boolean isSeatOccupiedOrRestristed(Long seatIdToCheck, ZonedDateTime fromDate, ZonedDateTime toDate ){
        Long floorId = findOne(seatIdToCheck).orElseThrow(() -> new BadRequestAlertException("Floor not found", Floor.class, "floor not found"))
            .getRoom().getFloor().getId();
        List<SeatDto> seats = getSeatsDetailsPerFloorInGivenPeriodOfTim(floorId,fromDate,toDate);
        if(seats.stream()
            .anyMatch(seatDto ->seatDto.getId().equals(seatIdToCheck) && seatDto.getAvailabilityStatus().equals(AvailabilityStatus.OCCUPIED) )){
            throw new BadRequestAlertException("Seat "+ seatIdToCheck + " already occupied", SeatReserved.class, "invalid request");
        } else if (seats.stream()
            .anyMatch(seatDto -> seatDto.getId().equals(seatIdToCheck) && seatDto.getAvailabilityStatus().equals(AvailabilityStatus.RESTRICTED))){
            throw new BadRequestAlertException("Seat "+ seatIdToCheck + " is restricted", SeatReserved.class, "invalid request");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<Seat> findAll() {
        log.debug("Request to get all Seats");
        return seatRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Seat> findAll(Pageable pageable) {
        log.debug("Request to get all Seats");
        return seatRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Seat> findOne(Long id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Seat> findAllByRoom(Pageable pageable, Long roomId) {
        log.debug("Request to get Seats from Room : {}", roomId);
        return seatRepository.findSeatsByRoomId(pageable, roomId);
    }
    public List<Seat> findAllByFloor(Long floorId){
        log.debug("Request to get Seats from Floor : {}", floorId);
        return seatRepository.findSeatsByFloorId(floorId);
    }

    @Transactional
    public List<SeatDto> getSeatsDetailsPerFloorInGivenPeriodOfTim(Long floorId, ZonedDateTime fromDate, ZonedDateTime toDate) {
        List<SeatDto> seats = seatMapper.toDto(findAllByFloor(floorId));
        List<SeatDto> occupiedSeats = handleOccupied(seats, fromDate, toDate);
        return handleRestriction(occupiedSeats, fromDate, toDate);
    }

    private List<SeatDto> handleRestriction(List<SeatDto> seats, ZonedDateTime fromDate, ZonedDateTime toDate) {
        final List<Restrictions> restrictions = restrictionsService.findByPeriodTime(fromDate, toDate);
        restrictions.forEach(restriction -> restrictionHandler.getUpdatedSeatsAfterRestriction(restriction, seats));
        return seats;
    }

    private List<SeatDto> handleOccupied(List<SeatDto> seats, ZonedDateTime fromDate, ZonedDateTime toDate) {
        return reservationService.getUpdatedSeatsAfterReservation(seats, fromDate, toDate);
    }

    @Transactional(readOnly = true)
    public Page<Seat> findAllByFloor(Pageable pageable, Long floorId) {
        log.debug("Request to get Seats from Floor : {}", floorId);
        return seatRepository.findSeatsByFloorId(pageable, floorId);
    }

    public void delete(Long id) {
        Seat seat = seatRepository.getReferenceById(id);
        if (!seat.getEquipments().isEmpty()) {
            throw new BadRequestAlertException("Object have children", Seat.class, "objectHaveChildren");
        }
        log.debug("Request to delete Seat : {}", id);
        seatRepository.deleteById(id);
    }


}
