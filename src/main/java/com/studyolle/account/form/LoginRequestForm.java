package com.studyolle.account.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class LoginRequestForm {
    private String email;
    private String password;
    private boolean rememberMe;
}
