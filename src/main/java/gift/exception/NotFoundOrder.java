package gift.exception;

import java.util.NoSuchElementException;

public class NotFoundOrder extends NoSuchElementException {

    public NotFoundOrder(String message) {super(message);}

}
