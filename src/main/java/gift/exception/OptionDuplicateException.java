package gift.exception;

public class OptionDuplicateException extends RuntimeException {
    public OptionDuplicateException(String duplicatedName) {
        super(duplicatedName + " is duplicated");
    }
}
