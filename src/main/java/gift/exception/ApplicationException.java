package gift.exception;


public class ApplicationException extends RuntimeException {

    private final int code;

    public ApplicationException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
