package gift.web.exception.unauthorized;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
    }
}
