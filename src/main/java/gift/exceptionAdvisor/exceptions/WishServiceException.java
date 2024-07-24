package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class WishServiceException extends GiftException{

    public WishServiceException(String message, HttpStatus responseStatus) {
        super(message, responseStatus);
    }
}
