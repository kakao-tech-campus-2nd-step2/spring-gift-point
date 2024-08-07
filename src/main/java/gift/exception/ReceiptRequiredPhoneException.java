package gift.exception;

public class ReceiptRequiredPhoneException extends BusinessException {
    public ReceiptRequiredPhoneException(ErrorCode errorCode) {
        super(errorCode);
    }
}
