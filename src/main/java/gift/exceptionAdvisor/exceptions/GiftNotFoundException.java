package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class GiftNotFoundException extends GiftException{

    public GiftNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
