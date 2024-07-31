package gift.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class RedissonLockException extends CustomException {
    private static final String DEFAULT_TITLE = "Service Unavailable";

    public RedissonLockException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, DEFAULT_TITLE);
    }
}
