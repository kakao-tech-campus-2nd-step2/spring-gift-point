package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class MemberServiceException extends GiftException {

    public MemberServiceException(String message, HttpStatus responseStatus) {
        super(message, responseStatus);
    }
}
