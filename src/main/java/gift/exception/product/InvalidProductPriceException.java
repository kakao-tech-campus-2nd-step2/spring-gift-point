package gift.exception.product;

import gift.exception.ProductException;
import org.springframework.http.HttpStatus;

public class InvalidProductPriceException extends ProductException {

    public InvalidProductPriceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
