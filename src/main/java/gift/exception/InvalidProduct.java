package gift.exception;

import java.util.NoSuchElementException;

public class InvalidProduct extends NoSuchElementException {

    public InvalidProduct(String message) {
        super(message);
    }
}
