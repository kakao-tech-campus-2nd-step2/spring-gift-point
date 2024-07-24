package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class ProductServiceException extends GiftException {

    public ProductServiceException(String message, HttpStatus responseStatus) {
        super(message, responseStatus);
    }
}
