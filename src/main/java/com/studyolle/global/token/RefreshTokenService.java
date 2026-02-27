package com.studyolle.global.token;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    // Refresh 토큰 유효기간 7일
    private final long refreshTokenValidSeconds = 7 * 24 * 60 * 60;

    public String createAndStoreRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidSeconds * 1000); // 초 → 밀리초
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtTokenProvider.getSigningKey())
                .compact();

        redisTemplate.opsForValue().set(
                getRedisKey(email),
                refreshToken,
                Duration.ofSeconds(refreshTokenValidSeconds)
        );
        return refreshToken;
    }


    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(getRedisKey(email));
    }

    public boolean validateRefreshToken(String email, String refreshToken) {
        String storedToken = getRefreshToken(email);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    /**
     * 로그아웃 시 혹은 RefreshToken 만료 시 삭제
     */
    public void deleteRefreshToken(String email) {
        redisTemplate.delete(getRedisKey(email));
    }

    private String getRedisKey(String email) {
        return "refresh_token:" + email;
    }

}
