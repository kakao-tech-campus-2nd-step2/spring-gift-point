package gift.exception;

import java.util.NoSuchElementException;

public class InvalidCategory extends NoSuchElementException {

    public InvalidCategory(String message) {super(message);}
}
