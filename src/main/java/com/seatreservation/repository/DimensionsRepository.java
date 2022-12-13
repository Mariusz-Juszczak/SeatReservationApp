package com.seatreservation.repository;

import com.seatreservation.domain.Dimensions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionsRepository extends JpaRepository<Dimensions, Long> {}
