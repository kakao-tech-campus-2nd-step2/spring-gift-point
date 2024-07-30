package gift.exception;

import java.util.List;

public class KakaoApiHasProblemException extends RuntimeException {

    private final List<Exception> exceptions;

    public KakaoApiHasProblemException(String message, List<Exception> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }
}
