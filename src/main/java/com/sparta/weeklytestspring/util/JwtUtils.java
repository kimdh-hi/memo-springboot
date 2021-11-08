package com.sparta.weeklytestspring.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    private final String SECRET = "secret";
    private final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;

    // 토큰 생성
    // Claim은 subject 역할의 username과 만료시간 두가지를 설정
    public String createToken(String subject) {
        Claims claims = Jwts.claims();
        claims.put("username", subject);

        return Jwts.builder()
                .claim("username", subject)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SIGNATURE, SECRET)
                .compact();
    }

    // 토큰의 Claim에서 username을 가져온다.
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaims(token);

        return String.valueOf(claims.get("username"));
    }

    // 토큰 유효여부 확인
    // 현재는 토큰의 만료시간만을 검증
    public Boolean isValidToken(String token) {
        Date expiration = getAllClaims(token).getExpiration();
        return expiration.after(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
