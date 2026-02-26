package com.studyolle.global.controller;

import com.studyolle.account.security.UserAccount;
import com.studyolle.notification.repository.NotificationRepository;
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
