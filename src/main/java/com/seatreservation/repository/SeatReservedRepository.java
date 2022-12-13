package com.seatreservation.repository;

import com.seatreservation.domain.SeatReserved;
import com.seatreservation.domain.User;
import com.seatreservation.domain.enumeration.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;


@Repository
public interface SeatReservedRepository extends JpaRepository<SeatReserved, Long> {

    @Query("select seatReserved from SeatReserved seatReserved where seatReserved.user.login = ?#{principal.username} AND seatReserved.toDate > :toDate")
    Page<SeatReserved> findSeatReservedByToDateAfter(Pageable pageable, @Param ("toDate") ZonedDateTime toDate);
    @Query("select seatReserved from SeatReserved seatReserved where seatReserved.user.login = ?#{principal.username} AND seatReserved.toDate < :toDate")
    Page<SeatReserved> findSeatReservedByToDateBefore(Pageable pageable, @Param ("toDate") ZonedDateTime toDate);

    @Query("select seatReserved from SeatReserved seatReserved where seatReserved.user.login = ?#{principal.username}")
    Page<SeatReserved> findByUserIsCurrentUser(Pageable pageable);

    Page<SeatReserved> findAllByReservationStatus(Pageable pageable, ReservationStatus ReservationStatus);

    @Query("select seatReserved from SeatReserved seatReserved " +
        "where seatReserved.reservationStatus = 'PENDING' " +
        "and :user in elements(seatReserved.seat.room.floor.building.location.locationAdmins)")
    Page<SeatReserved> findAllByReservationStatusAndByLocationAdmin(Pageable pageable,
                                                         @Param("user") User user);
    @Query("select seatReserved from SeatReserved seatReserved " +
        "where :user in elements(seatReserved.seat.room.floor.building.location.locationAdmins)")
    Page<SeatReserved> findAllByLocationAdmin(Pageable pageable,
                                                         @Param("user") User user);


    @Query("select seatReserved " +
        "from SeatReserved seatReserved " +
        "where seatReserved.fromDate >= :fromDate " +
        "and ( seatReserved.toDate <= :toDate or seatReserved.toDate is null ) " +
        "and seatReserved.seat.id in :seatsId " +
        "and seatReserved.reservationStatus in :reservationStatuses")
    List<SeatReserved> findByReservedSeatsInGivenPeriodOfTime(@Param("fromDate") ZonedDateTime fromDate,
                                                              @Param("toDate") ZonedDateTime toDate,
                                                              @Param("seatsId") List<Long> seatsId,
                                                              @Param("reservationStatuses") Set<ReservationStatus> reservationStatuses);

    @Query("SELECT seatReserved from SeatReserved seatReserved " +
        "WHERE seatReserved.user.login = ?#{principal.username} " +
        "AND seatReserved.fromDate <= :toDate " +
        "AND seatReserved.toDate >= :fromDate " +
        "AND seatReserved.reservationStatus IN :status")
    List<SeatReserved> findByUserAndBetweenDatesAndWithStatus(@Param ("fromDate") ZonedDateTime fromDate,
                                                              @Param ("toDate") ZonedDateTime toDate,
                                                              @Param ("status") Set<ReservationStatus> status);

}
