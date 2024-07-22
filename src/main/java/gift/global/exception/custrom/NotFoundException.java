package gift.global.exception.custrom;


import gift.global.exception.ErrorCode;

public class NotFoundException extends CustomException{
    public NotFoundException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
}
