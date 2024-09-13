package core.contest5.global.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtIssuer {

    //    private static String SECRET_KEY = "secretKeyForJsonWebTokenTutorial";
    private static String SECRET_KEY;

    private static Long ACCESS_EXPIRE_TIME;
    private static Long REFRESH_EXPIRE_TIME;

    public static final String KEY_ROLES = "roles";

    @Value("${jwt.token.secret-key}")
    public void setSecretKey(String secretKey) {
        JwtIssuer.SECRET_KEY = secretKey;
    }

    @Value("${jwt.token.accesstoken-expired-time-ms}")
    public void setAccessTokenExpiredTimeMs(Long accessTokenExpiredTimeMs) {
        JwtIssuer.ACCESS_EXPIRE_TIME = accessTokenExpiredTimeMs;
    }

    @Value("${jwt.token.refreshtoken-expired-time-ms}")
    public void setRefreshTokenExpiredTimeMs(Long refreshTokenExpiredTimeMs) {
        JwtIssuer.REFRESH_EXPIRE_TIME = refreshTokenExpiredTimeMs;
    }

    @PostConstruct
    void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtDto createToken(String userEmail, String role) {
        String encryptedEmail = Aes256Util.encrypt(userEmail);

        Claims claims = Jwts.claims().setSubject(encryptedEmail);
        claims.put(KEY_ROLES, role);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        claims.setSubject(encryptedEmail);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getSubject(Claims claims) {
        return Aes256Util.decrypt(claims.getSubject());
    }

    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            throw new BadCredentialsException("유효한 토큰이 아닙니다.");
        }
        return claims;
    }


}
