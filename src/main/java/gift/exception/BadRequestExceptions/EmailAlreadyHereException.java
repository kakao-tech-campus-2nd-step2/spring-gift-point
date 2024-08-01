package gift.exception.BadRequestExceptions;

public class EmailAlreadyHereException extends BadRequestException {
    private EmailAlreadyHereException() { super(); }
    public EmailAlreadyHereException(String message) { super(message); }
}
