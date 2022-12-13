package com.seatreservation.service;

import com.seatreservation.domain.SeatReserved;
import com.seatreservation.domain.User;
import com.seatreservation.domain.enumeration.ReservationStatus;
import com.seatreservation.repository.SeatReservedRepository;
import com.seatreservation.repository.UserRepository;
import com.seatreservation.security.SecurityUtils;
import com.seatreservation.web.rest.errors.BadRequestAlertException;
import com.seatreservation.web.rest.errors.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeatReservedService {

    private final Logger log = LoggerFactory.getLogger(SeatReservedService.class);
    private final SeatReservedRepository seatReservedRepository;
    private final UserRepository userRepository;
    private final ApplicationNotificationService applicationNotificationService;

    public SeatReservedService(SeatReservedRepository seatReservedRepository, UserRepository userRepository, ApplicationNotificationService applicationNotificationService) {
        this.seatReservedRepository = seatReservedRepository;
        this.userRepository = userRepository;
        this.applicationNotificationService = applicationNotificationService;
    }

    public SeatReserved save(SeatReserved seatReserved) {
        if (seatReserved.getId() != null) {
            throw new BadRequestAlertException("A new seatReserved cannot already have an ID", SeatReserved.class, "id exists");
        }
        log.debug("Request to save SeatReserved : {}", seatReserved);

        seatReserved.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(null)).orElse(null));

        List<SeatReserved> currentReservations = seatReservedRepository.findByUserAndBetweenDatesAndWithStatus(seatReserved.getFromDate(), seatReserved.getToDate(), EnumSet.of(ReservationStatus.ACTIVE, ReservationStatus.PENDING));
        if (!currentReservations.isEmpty()) {
            seatReserved.setReservationStatus(ReservationStatus.PENDING);
        } else {
            seatReserved.setReservationStatus(ReservationStatus.ACTIVE);
        }

        SeatReserved savedSeatReserved = seatReservedRepository.save(seatReserved);
        return savedSeatReserved;
    }

    public SeatReserved update(SeatReserved seatReserved, Long id) {
        if (seatReserved.getId() == null) {
            throw new BadRequestAlertException("Invalid id", SeatReserved.class, "id null");
        }
        if (!Objects.equals(id, seatReserved.getId())) {
            throw new BadRequestAlertException("Invalid ID", SeatReserved.class, "id invalid");
        }

        if (!seatReservedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", SeatReserved.class, "id not found");
        }
        log.debug("Request to save SeatReserved : {}", seatReserved);
        return seatReservedRepository.save(seatReserved);
    }

    public boolean seatReservedDateValidation(ZonedDateTime fromDate, ZonedDateTime toDate) {
        return fromDate.compareTo(toDate) <= 0;
    }

    public boolean seatReservedFromDateInPastValidation(ZonedDateTime fromDate) {
        return ZonedDateTime.now().compareTo(fromDate) <= 0;
    }

    @Transactional(readOnly = true)
    public List<SeatReserved> findByReservedSeatsInGivenPeriodOfTime(ZonedDateTime fromDate, ZonedDateTime toDate, List<Long> seatIds) {
        if (!seatReservedFromDateInPastValidation(fromDate)) {
            throw new BadRequestAlertException("fromDate is less than current date", SeatReserved.class, "fromDateLessThanCurrent");
        }
        if (!seatReservedDateValidation(fromDate, toDate)) {
            throw new BadRequestAlertException("fromDate is greater that toDate", SeatReserved.class, "wrongTimePeriod");
        } else
            return seatReservedRepository.findByReservedSeatsInGivenPeriodOfTime(fromDate, toDate, seatIds, EnumSet.of(ReservationStatus.ACTIVE, ReservationStatus.PENDING));
    }

    @Transactional(readOnly = true)
    public Page<SeatReserved> findAll(Pageable pageable) {
        log.debug("Request to get all Reserved seats");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UserNotFoundException("Current user login not found"))).orElseThrow(() -> new UserNotFoundException("User could not be found"));
        if (user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName()))) {
            return seatReservedRepository.findAll(pageable);
        }
        return seatReservedRepository.findAllByLocationAdmin(pageable, user);
    }

    @Transactional(readOnly = true)
    public Page<SeatReserved> findPending(Pageable pageable) {
        log.debug("Request to get Reserved seats with Pending status");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UserNotFoundException("Current user login not found"))).orElseThrow(() -> new UserNotFoundException("User could not be found"));
        if (user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName()))) {
            return seatReservedRepository.findAllByReservationStatus(pageable, ReservationStatus.PENDING);
        }
        return seatReservedRepository.findAllByReservationStatusAndByLocationAdmin(pageable, user);
    }

    @Transactional(readOnly = true)
    public Page<SeatReserved> findAllByCurrentUser(Pageable pageable) {
        log.debug("Request to get all Reserved seats By Current user");
        return seatReservedRepository.findByUserIsCurrentUser(pageable);
    }

    @Transactional(readOnly = true)
    public List<SeatReserved> findAllWhereSeatIsNull() {
        log.debug("Request to get all Reserved seats where Seat is null");
        return seatReservedRepository.findAll().stream().filter(seatReserved -> seatReserved.getSeat() == null).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<SeatReserved> findOne(Long id) {
        log.debug("Request to get SeatReserved : {}", id);
        return seatReservedRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<SeatReserved> findUpcomingReservation(Pageable pageable) {
        ZonedDateTime date = ZonedDateTime.now();
        log.debug("Request to get SeatReserved after current date : {}", date);
        return seatReservedRepository.findSeatReservedByToDateAfter(pageable, date);
    }

    @Transactional(readOnly = true)
    public Page<SeatReserved> findRecentReservation(Pageable pageable) {
        ZonedDateTime date = ZonedDateTime.now();
        log.debug("Request to get SeatReserved before current date : {}", date);
        return seatReservedRepository.findSeatReservedByToDateBefore(pageable, date);
    }

    public void delete(Long id) {
        log.debug("Request to delete SeatReserved : {}", id);
        seatReservedRepository.deleteById(id);
    }

    @Transactional
    public void cancelReservation(Long id) {
        SeatReserved seatReserved = changeReservationStatus(id, ReservationStatus.CANCELLED);
        applicationNotificationService.createReservationHasBeenCancelledNotification(seatReserved);
    }

    @Transactional
    public void createReservation(Long id) {
        SeatReserved seatReserved = findOne(id)
            .orElseThrow(() -> new EntityNotFoundException(id, SeatReserved.class));
        applicationNotificationService.createReservationHasBeenCreatedNotification(seatReserved);
    }

    @Transactional
    public void approveReservation(Long id) {
        SeatReserved seatReserved = changeReservationStatus(id, ReservationStatus.ACTIVE);
        applicationNotificationService.createReservationHasBeenApprovedNotification(seatReserved);
    }

    private SeatReserved changeReservationStatus(Long id, ReservationStatus reservationStatus) {
        SeatReserved seatReserved = findOne(id)
            .orElseThrow(() -> new EntityNotFoundException(id, SeatReserved.class));
        log.debug("Request to change status for SeatReserved : {}", id);
        seatReserved.setReservationStatus(reservationStatus);
        return seatReserved;
    }


}
