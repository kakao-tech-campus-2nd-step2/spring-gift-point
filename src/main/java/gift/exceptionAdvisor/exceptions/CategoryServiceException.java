package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryServiceException extends GiftException {

    public CategoryServiceException(String message, HttpStatus responseStatus) {
        super(message, responseStatus);
    }
}
