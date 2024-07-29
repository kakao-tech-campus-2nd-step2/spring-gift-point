package gift.global.validate;

public class InvalidAuthRequestException extends RuntimeException {

    public InvalidAuthRequestException() {
        super();
    }

    public InvalidAuthRequestException(String message) {
        super(message);
    }

    public InvalidAuthRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthRequestException(Throwable cause) {
        super(cause);
    }

}
