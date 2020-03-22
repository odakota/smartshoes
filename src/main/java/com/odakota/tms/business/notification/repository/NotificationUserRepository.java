package com.odakota.tms.business.notification.repository;

import com.odakota.tms.business.notification.entity.NotificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    Optional<NotificationUser> findByDeletedFlagFalseAndNotificationIdAndUserId(Long notificationId, Long userId);
}
