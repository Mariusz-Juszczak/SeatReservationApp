package com.seatreservation.service;

import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.domain.User;
import com.seatreservation.domain.enumeration.NotificationPriority;
import com.seatreservation.domain.enumeration.NotificationState;
import com.seatreservation.repository.NotificationAlertRepository;
import com.seatreservation.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

import static com.seatreservation.domain.enumeration.NotificationState.*;

@Service
@Transactional
public class NotificationAlertService {

    private final static String TITLE_FOR_CANCELLED_RESERVATION = "Cancelled Reservation";

    private final Logger log = LoggerFactory.getLogger(NotificationAlertService.class);

    private final NotificationAlertRepository notificationAlertRepository;

    public NotificationAlertService(NotificationAlertRepository notificationAlertRepository) {
        this.notificationAlertRepository = notificationAlertRepository;
    }

    public NotificationAlert save(NotificationAlert notificationAlert) {
        log.debug("Request to save NotificationAlert : {}", notificationAlert);
        if (notificationAlert.getId() != null) {
            throw new BadRequestAlertException("A new notificationAlert cannot already have an ID", NotificationAlert.class, "id exists");
        }
        return notificationAlertRepository.save(notificationAlert);
    }

    public NotificationAlert update(NotificationAlert notificationAlert, Long id) {
        if (notificationAlert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", NotificationAlert.class, "id null");
        }
        if (!Objects.equals(id, notificationAlert.getId())) {
            throw new BadRequestAlertException("Invalid ID", NotificationAlert.class, "id invalid");
        }

        if (!notificationAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", NotificationAlert.class, "id not found");
        }
        log.debug("Request to save NotificationAlert : {}", notificationAlert);
        return notificationAlertRepository.save(notificationAlert);
    }

    @Transactional(readOnly = true)
    public Page<NotificationAlert> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationAlerts");
        return notificationAlertRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<NotificationAlert> findOne(Long id) {
        log.debug("Request to get NotificationAlert : {}", id);
        return notificationAlertRepository.findById(id);
    }

    public void updateStateToRead(Long id) {
        log.debug("Request to mark as read NotificationAlert: {}", id);
        notificationAlertRepository.markAsRead(id);
    }

    public void updateStateToDeleted(Long id) {
        log.debug("Request to mark as delete NotificationAlert : {}", id);
        notificationAlertRepository.markAsDeleted(id);
    }

    @Transactional(readOnly = true)
    public Page<NotificationAlert> findAllByUser(Pageable pageable) {
        log.debug("Request to get NotificationAlerts by User");
        return findByUserIdAndNotificationState(pageable, EnumSet.of(NEW, READ));
    }

    @Transactional(readOnly = true)
    public Page<NotificationAlert> findNewByUser(Pageable pageable) {
        log.debug("Request to get new NotificationAlerts by User");
        return findByUserIdAndNotificationState(pageable, EnumSet.of(NEW));
    }

    @Transactional
    public void createNewNotificationForUser(User user, String message) {
        NotificationAlert notificationAlert = new NotificationAlert();
        notificationAlert.setUser(user);
        notificationAlert.setCreatedAt(ZonedDateTime.now());
        notificationAlert.setNotificationPriority(NotificationPriority.HIGH);
        notificationAlert.setNotificationState(NotificationState.NEW);
        notificationAlert.setMessage(message);
        notificationAlert.setTitle(TITLE_FOR_CANCELLED_RESERVATION);
        save(notificationAlert);
    }

    private Page<NotificationAlert> findByUserIdAndNotificationState(Pageable pageable, Set<NotificationState> notificationStates) {
        log.debug("Request to get NotificationAlerts by User");
        return notificationAlertRepository.findByUserAndNotificationStateInOrderByCreatedAtDesc(pageable, notificationStates);
    }
}
