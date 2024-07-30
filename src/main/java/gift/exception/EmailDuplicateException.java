package gift.exception;

public class EmailDuplicateException extends RuntimeException {

    public EmailDuplicateException(String email) {
        super(email + " already in use");
    }
}
