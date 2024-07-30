package gift.exception;

public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(String message) {
        super(message);
    }

}
