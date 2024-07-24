package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class GiftException extends RuntimeException {

    private final HttpStatus responseStatus;

    public GiftException(String message, HttpStatus responseStatus) {
        super(message);
        this.responseStatus = responseStatus;
    }


    public HttpStatusCode getStatusCode() {
        return responseStatus;
    }
}
