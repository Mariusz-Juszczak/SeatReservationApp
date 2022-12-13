package com.seatreservation.service;

import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.domain.enumeration.NotificationPriority;
import com.seatreservation.domain.enumeration.NotificationState;
import com.seatreservation.repository.NotificationAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class NotificationAlertServiceTest {

    private static final String default_title = "testTitle";

    private static final String default_message = "testMessage";

    private static final NotificationPriority default_notificationPriority = NotificationPriority.HIGH;

    private static final  NotificationState default_notificationState = NotificationState.NEW;

    @Autowired
    private NotificationAlertService notificationAlertService;

    @Autowired
    private NotificationAlertRepository notificationAlertRepository;

    private NotificationAlert notificationAlert;

    @BeforeEach
    public void init() {
        notificationAlert = new NotificationAlert();
        notificationAlert.setTitle(default_title);
        notificationAlert.setMessage(default_message);
        notificationAlert.setNotificationPriority(default_notificationPriority);
        notificationAlert.setNotificationState(default_notificationState);
        notificationAlert.setCreatedAt(ZonedDateTime.now());
    }

    @Test
    @Transactional
    void assertThatNotificationAlertIsSaved() {
        NotificationAlert maybeSavedNotificationAlert = notificationAlertService.save(notificationAlert);
        assertThat(maybeSavedNotificationAlert).isEqualTo(notificationAlertRepository.findById(1001L).get());
        notificationAlertRepository.delete(maybeSavedNotificationAlert);
    }
}
