package ru.sshibko.sas.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sshibko.sas.security.AppUserDetails;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

//TODO remove deprecated methods
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() +
                        tokenExpiration.toMillis()))
                .signWith(generateKey(jwtSecret))
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(generateKey(jwtSecret))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(generateKey(jwtSecret))
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Key generateKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

}
