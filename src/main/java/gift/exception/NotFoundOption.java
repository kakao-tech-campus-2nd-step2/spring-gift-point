package gift.exception;

import java.util.NoSuchElementException;

public class NotFoundOption extends NoSuchElementException {

    public NotFoundOption(String message) {super(message);}
}
