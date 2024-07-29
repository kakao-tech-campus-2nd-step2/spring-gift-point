package gift.common.exception;

public class InvalidProductDataException extends RuntimeException {
    public InvalidProductDataException(String value) {
        super(value + "을(를) 다시 입력해주세요.");
    }
}
