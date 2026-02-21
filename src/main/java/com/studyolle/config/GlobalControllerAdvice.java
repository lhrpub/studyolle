package com.studyolle.config;

import com.studyolle.account.UserAccount;
import com.studyolle.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final NotificationRepository notificationRepository;

    @ModelAttribute("hasNotification")
    public boolean hasNotification(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserAccount userAccount)) {
            return false;
        }
        return notificationRepository.countByAccountAndChecked(
                userAccount.getAccount(), false) > 0;
    }
}
