package com.seatreservation.service;

import com.seatreservation.domain.enumeration.NotificationState;
import com.seatreservation.repository.UserRepository;
import com.seatreservation.security.SecurityUtils;
import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.domain.Restrictions;
import com.seatreservation.domain.SeatReserved;
import com.seatreservation.domain.User;
import com.seatreservation.domain.enumeration.ApplicationNotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ApplicationNotificationService {

    private final NotificationAlertService notificationAlertService;
    private final UserRepository userRepository;

    public ApplicationNotificationService(NotificationAlertService notificationAlertService, UserRepository userRepository) {
        this.notificationAlertService = notificationAlertService;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReservationHasBeenCancelledNotification(SeatReserved seatReserved) {
        NotificationAlert notificationAlert = new NotificationAlert()
            .title(ApplicationNotificationType.RESERVATION_HAS_BEEN_CANCELLED.getTitle())
            .message(ApplicationNotificationType.RESERVATION_HAS_BEEN_CANCELLED.getMessage() + seatReserved.getReservationName())
            .notificationState(NotificationState.NEW)
            .notificationPriority(ApplicationNotificationType.RESERVATION_HAS_BEEN_CANCELLED.getPriority())
            .user(seatReserved.getUser());
        notificationAlertService.save(notificationAlert);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReservationHasBeenCreatedNotification(SeatReserved seatReserved) {
        NotificationAlert notificationAlert = new NotificationAlert()
            .title(ApplicationNotificationType.RESERVATION_HAS_BEEN_CREATED.getTitle())
            .message(ApplicationNotificationType.RESERVATION_HAS_BEEN_CREATED.getMessage() + seatReserved.getReservationName())
            .notificationState(NotificationState.NEW)
            .notificationPriority(ApplicationNotificationType.RESERVATION_HAS_BEEN_CREATED.getPriority())
            .user(getCurrentUser());
        notificationAlertService.save(notificationAlert);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReservationHasBeenApprovedNotification(SeatReserved seatReserved) {
        NotificationAlert notificationAlert = new NotificationAlert()
            .title(ApplicationNotificationType.RESERVATION_HAS_BEEN_APPROVED.getTitle())
            .message(ApplicationNotificationType.RESERVATION_HAS_BEEN_APPROVED.getMessage() + seatReserved.getReservationName())
            .notificationState(NotificationState.NEW)
            .notificationPriority(ApplicationNotificationType.RESERVATION_HAS_BEEN_APPROVED.getPriority())
            .user(seatReserved.getUser());
        notificationAlertService.save(notificationAlert);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNewRestrictionNotifications(Restrictions restrictions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        String restrictionName = restrictions.getName() + " | Type: " + restrictions.getRestrictionType().getMessage(restrictions.getRestrictionValue()) + " | from: " + restrictions.getFromDate().format(formatter);
        getAllUsers().forEach(user -> createNewRestrictionNotification(restrictionName, user));
    }

    private void createNewRestrictionNotification(String restrictionName, User user) {
        NotificationAlert notificationAlert = new NotificationAlert()
            .title(ApplicationNotificationType.NEW_RESTRICTION_HAS_BEEN_CREATED.getTitle())
            .message(ApplicationNotificationType.NEW_RESTRICTION_HAS_BEEN_CREATED.getMessage() + restrictionName)
            .notificationState(NotificationState.NEW)
            .notificationPriority(ApplicationNotificationType.NEW_RESTRICTION_HAS_BEEN_CREATED.getPriority())
            .user(user);
        notificationAlertService.save(notificationAlert);
    }

    private User getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElse(null);
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
