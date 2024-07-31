package gift.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends CustomException {
    private static final String DEFAULT_TITLE = "Access Denied";

    public AuthorizationException(String message) {
        super(message, HttpStatus.FORBIDDEN, DEFAULT_TITLE);
    }

    public AuthorizationException(String message, HttpStatus status, String title) {
        super(message, status, title);
    }
}
