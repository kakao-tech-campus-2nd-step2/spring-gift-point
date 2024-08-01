package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class GiftForbiddenException extends GiftException{

    public GiftForbiddenException(String message) {
        super(message,HttpStatus.FORBIDDEN);
    }

}
