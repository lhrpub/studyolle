package com.studyolle.account;

import com.studyolle.account.form.LoginRequestForm;
import com.studyolle.account.form.SignUpForm;
import com.studyolle.account.repository.AccountRepository;
import com.studyolle.account.service.AccountService;
import com.studyolle.account.validator.SignUpFormValidator;
import com.studyolle.account.entity.Account;
import com.studyolle.global.annotaiton.CurrentAccount;
import com.studyolle.global.token.JwtTokenProvider;
import com.studyolle.global.token.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @PostMapping("/login")
    public String login( LoginRequestForm loginRequestForm, HttpServletResponse response) {
        try {
            String jwtToken = accountService.loginWithPassword(loginRequestForm.getEmail(), loginRequestForm.getPassword());

            Cookie accessCookie = new Cookie("ACCESS_TOKEN", jwtToken);
            accessCookie.setHttpOnly(true);  // JS에서 못읽게
            accessCookie.setSecure(false);    // HTTPS
            accessCookie.setPath("/");
            accessCookie.setMaxAge(30 * 60); // 30분
            response.addCookie(accessCookie);

            if (loginRequestForm.isRememberMe()){
                String refreshToken = refreshTokenService.createAndStoreRefreshToken(loginRequestForm.getEmail());

                Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
                refreshCookie.setHttpOnly(true);  // JS에서 못읽게
                refreshCookie.setSecure(false);    // HTTPS
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 30분
                response.addCookie(refreshCookie);
            } else {
                Cookie refreshCookie = new Cookie("REFRESH_TOKEN", "");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(false);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(0); // 쿠키 삭제
                response.addCookie(refreshCookie);
            }

            return "redirect:/";

        }catch (RuntimeException e){
            return "redirect:/login?error";
        }
    }

    @PostMapping("/logout")
    public String logout(@CurrentAccount Account account, HttpServletResponse response) {
        // 1. Redis에 저장된 RefreshToken 삭제
        refreshTokenService.deleteRefreshToken(account.getEmail());

        // 2. 클라이언트 쿠키 삭제
        Cookie accessCookie = new Cookie("ACCESS_TOKEN", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false); // HTTPS 환경이면 true
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm" , new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm , Errors errors, HttpServletResponse response){
        if (errors.hasErrors()){
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        String token = accountService.loginAfterSignUp(account);

        Cookie cookie = new Cookie("ACCESS_TOKEN", token);
        cookie.setHttpOnly(true);  // JS에서 못읽게
        cookie.setSecure(false);    // HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30분
        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model , HttpServletResponse response){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if (account == null){
            model.addAttribute("error","wrong.email");
            return view;
        }

        if (!account.isValidToken(token)){
            model.addAttribute("error", "wrong.token");
            return view;
        }

        String jwtToken = accountService.completeSignUp(account);

        Cookie cookie = new Cookie("ACCESS_TOKEN", jwtToken);
        cookie.setHttpOnly(true);  // JS에서 못읽게
        cookie.setSecure(false);    // HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30분
        response.addCookie(cookie);

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model){
        model.addAttribute("email" , account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model){
        if (!account.canSendConfirmEmail()){
            model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
            model.addAttribute("email" , account.getEmail());
            return "account/check-email";
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentAccount Account account) {
        Account accountToView = accountService.getAccount(nickname);
        model.addAttribute(accountToView);
        model.addAttribute("isOwner", accountToView.equals(account));
        return "account/profile";
    }

    @GetMapping("/email-login")
    public String emailLoginForm(){
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendEmailLoginLink(String email, Model model, RedirectAttributes attributes) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }

        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
            return "account/email-login";
        }

        accountService.sendLoginLink(account);
        attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/logged-in-by-email";
        if (account == null || !account.isValidToken(token)) {
            model.addAttribute("error", "로그인할 수 없습니다.");
            return view;
        }

        accountService.loginAfterSignUp(account);
        return view;
    }

}
