package com.studyolle.notification;

import com.studyolle.domain.Account;
import com.studyolle.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    long countByAccountAndChecked(Account account, boolean checked);
}
