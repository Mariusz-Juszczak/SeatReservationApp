package com.seatreservation.repository;

import com.seatreservation.domain.Coordinates;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {}
