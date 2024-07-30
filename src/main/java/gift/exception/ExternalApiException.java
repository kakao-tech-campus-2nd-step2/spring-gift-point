package gift.exception;

import org.springframework.http.HttpStatus;

public class ExternalApiException extends CustomException {

    public ExternalApiException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
