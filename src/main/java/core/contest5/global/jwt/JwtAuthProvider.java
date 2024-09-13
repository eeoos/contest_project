package core.contest5.global.jwt;

import core.contest5.member.service.MemberDomain;
import core.contest5.member.service.MemberReader;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthProvider {

    private final UserDetailsService userDetailsService;
    private final JwtIssuer jwtIssuer;
    private final MemberReader memberReader;

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = jwtIssuer.getClaims(token);
        if (claims == null) {
            return false;
        }

        /*
         * 추가 검증 로직
         */

        return true;
    }

    public boolean validateToken(JwtDto jwtDto) {
        if (!StringUtils.hasText(jwtDto.getAccessToken())
            || !StringUtils.hasText(jwtDto.getRefreshToken())) {
            return false;
        }

        Claims accessClaims = jwtIssuer.getClaims(jwtDto.getAccessToken());
        Claims refreshClaims = jwtIssuer.getClaims(jwtDto.getRefreshToken());

        /*
         * 추가 검증 로직
         */

        return accessClaims != null && refreshClaims != null
            && jwtIssuer.getSubject(accessClaims).equals(jwtIssuer.getSubject(refreshClaims));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtIssuer.getClaims(token);
        String email = jwtIssuer.getSubject(claims);
        log.info("email={}", email);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        log.info("userDetails={}", userDetails);
        MemberDomain member = memberReader.read(email);

        log.info("userDetails.getAuthorities()={}", userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(member, null,
            userDetails.getAuthorities());
    }

}
