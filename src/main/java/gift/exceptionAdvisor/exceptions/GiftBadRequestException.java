package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class GiftBadRequestException extends GiftException{

    public GiftBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
