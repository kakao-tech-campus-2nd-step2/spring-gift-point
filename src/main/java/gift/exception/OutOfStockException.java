package gift.exception;

import org.springframework.http.HttpStatus;

public class OutOfStockException extends CustomException {

    public OutOfStockException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
