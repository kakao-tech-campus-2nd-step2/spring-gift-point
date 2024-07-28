package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class GiftUnauthorizedException extends GiftException{

    public GiftUnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
