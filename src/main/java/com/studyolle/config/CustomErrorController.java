package com.studyolle.config;

import com.studyolle.account.CurrentAccount;
import com.studyolle.domain.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(@CurrentAccount Account account, HttpServletRequest req, RuntimeException e) {
        String originalUri =
                (String) req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), originalUri);
        } else {
            log.info("requested '{}'", originalUri);
        }
        log.error("bad request", e);
        return "error";
    }
}
