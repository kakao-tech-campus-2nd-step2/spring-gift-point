package gift.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateDataException extends CustomException {
    private static final String DEFAULT_TITLE = "Duplicate Data";

    public DuplicateDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST, DEFAULT_TITLE);
    }

    public DuplicateDataException(String message, String title) {
        super(message, HttpStatus.BAD_REQUEST, title);
    }

    public DuplicateDataException(String message, HttpStatus status, String title) {
        super(message, status, title);
    }
}
