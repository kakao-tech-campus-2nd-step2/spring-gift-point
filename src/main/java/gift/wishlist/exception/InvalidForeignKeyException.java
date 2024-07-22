package gift.wishlist.exception;

public class InvalidForeignKeyException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "존재하지 않는 외래 키를 참조하고 있습니다.";

    public InvalidForeignKeyException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidForeignKeyException(String message) {
        super(message);
    }
}