package core.contest5.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.contest5.global.ApiResponse;
import core.contest5.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(ErrorCode.INVALID_TOKEN, "error in token validation")));
    }
}
