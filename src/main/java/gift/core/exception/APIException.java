package gift.core.exception;

public class APIException extends RuntimeException {
    private final ErrorCode code;

    public APIException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
