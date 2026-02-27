package com.studyolle.global.token;

import com.studyolle.account.entity.Account;
import com.studyolle.account.security.UserAccount;
import com.studyolle.account.service.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request , "ACCESS_TOKEN");

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            String refreshToken = resolveToken(request, "REFRESH_TOKEN");
            if (refreshToken != null) {
                String userEmail = jwtTokenProvider.getUserEmail(refreshToken);

                if (refreshTokenService.validateRefreshToken(userEmail, refreshToken)) {

                    String newAccessToken = jwtTokenProvider.createAccessToken(userEmail, List.of("ROLE_USER"));

                    Cookie accessCookie = new Cookie("ACCESS_TOKEN", newAccessToken);
                    accessCookie.setHttpOnly(true);
                    accessCookie.setSecure(false); // HTTPS 환경이면 true
                    accessCookie.setPath("/");
                    accessCookie.setMaxAge(30 * 60); // 30분
                    response.addCookie(accessCookie);

                    setAuthentication(newAccessToken);
                }
            }

        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        String userEmail = jwtTokenProvider.getUserEmail(token);
        Account account = accountService.loadAccountByUserEmail(userEmail);
        UserDetails userDetails = new UserAccount(account);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String resolveToken(HttpServletRequest request , String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
