package gift.Exception.Option;

public class OptionDuplicatedException extends RuntimeException {

    public OptionDuplicatedException() {
        super();
    }

    public OptionDuplicatedException(String message) {
        super(message);
    }

    public OptionDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected OptionDuplicatedException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
