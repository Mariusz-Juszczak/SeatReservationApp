package com.seatreservation.service;

import com.seatreservation.domain.Seat;
import com.seatreservation.domain.SeatReserved;
import com.seatreservation.repository.SeatReservedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SeatReservedServiceTest {

    private SeatReservedRepository seatReservedRepository;
    private SeatReservedService seatReservedService;
    private ApplicationNotificationService applicationNotificationService;

    private static final Long SEAT_RESERVED_ID = 1L;

    @BeforeEach
    void setUp() {
        seatReservedService = new SeatReservedService(seatReservedRepository, null, applicationNotificationService);
    }

    @Test
    void isSeatReservedAvailable() {
        //given
        Seat seat = createSeat();
        SeatReserved seatReserved = createSeatReserved(seat);

        //when
        seatReservedRepository.save(seatReserved);
        //boolean underTestFlag = seatReservedService.isSeatReservedAvailable(SEAT_RESERVED_ID);
        //then
        //assertThat(underTestFlag).isEqualTo(true);
        //        Mockito.when(nameService.getUserName("SomeId")).thenReturn("Mock user name");
    }

    private static SeatReserved createSeatReserved(Seat seat) {
        SeatReserved seatReserved = new SeatReserved();
        seatReserved.setId(SEAT_RESERVED_ID);
        seatReserved.setSeat(seat);
        return seatReserved;
    }

    private static Seat createSeat() {
        Seat seat = new Seat ();
        seat.setId(1L);

        return seat;
    }
}
