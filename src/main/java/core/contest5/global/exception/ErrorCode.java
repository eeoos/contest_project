package core.contest5.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "Unauthorized user"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "resource not found"),
    NO_RESULT(HttpStatus.NOT_FOUND, "The search returned no results.");
    private final HttpStatus status;
    private final String message;
}
