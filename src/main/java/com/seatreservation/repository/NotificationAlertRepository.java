package com.seatreservation.repository;

import com.seatreservation.domain.NotificationAlert;
import com.seatreservation.domain.enumeration.NotificationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface NotificationAlertRepository extends JpaRepository<NotificationAlert, Long> {

    @Modifying
    @Query ("update NotificationAlert notificationAlert SET notificationAlert.notificationState = 'READ' WHERE notificationAlert.notificationState = 'NEW' AND notificationAlert.id = :notificationId")
    void markAsRead(@Param ("notificationId")Long notificationId);

    @Modifying
    @Query ("update NotificationAlert notificationAlert SET notificationAlert.notificationState = 'DELETED' WHERE notificationAlert.id = :notificationId")
    void markAsDeleted(@Param ("notificationId")Long notificationId);


    @Query("select notificationAlert from NotificationAlert notificationAlert  where notificationAlert.user.login = ?#{principal.username} AND notificationAlert.notificationState IN :notificationStates")
    Page<NotificationAlert> findByUserAndNotificationStateInOrderByCreatedAtDesc(Pageable pageable, @Param("notificationStates") Set<NotificationState> notificationStates);
}
