package gift.exception;

import org.springframework.http.HttpStatus;

public class InvalidCategoryInfoException extends CustomException {

    public InvalidCategoryInfoException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
