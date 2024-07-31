package gift.doamin.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRefreshTokenException extends ResponseStatusException {

    public InvalidRefreshTokenException() {
        super(HttpStatus.FORBIDDEN, "유효하지 않은 refresh 토큰입니다.");
    }
}
