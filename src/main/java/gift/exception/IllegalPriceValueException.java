package gift.exception;

import org.springframework.http.HttpStatus;

public class IllegalPriceValueException extends CustomException {

    public IllegalPriceValueException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
