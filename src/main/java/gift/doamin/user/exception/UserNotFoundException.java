package gift.doamin.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException() {
        super(HttpStatus.FORBIDDEN, "일치하는 계정이 없습니다.");
    }
}
