package com.seatreservation.repository;

import com.seatreservation.domain.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("select s from Seat s join Room r on s.room.id = r.id join Floor f on f.id = r.floor.id where f.building.id = ?1")
    List<Seat> findSeatsByBuildingId(Long buildingId);
    Page<Seat> findSeatsByRoomId(Pageable pageable, @Param("room_id") Long roomId);

    @Query("select s from Seat s join Room r on s.room.id = r.id where r.floor.id = :floor_id")
    List<Seat> findSeatsByFloorId(@Param("floor_id") Long floorId);
    @Query("select s from Seat s join Room r on s.room.id = r.id where r.floor.id = :floor_id")
    Page<Seat> findSeatsByFloorId(Pageable pageable, @Param("floor_id") Long floorId);
}
