package gift.exception.customException;

import gift.exception.ErrorCode;
import org.springframework.validation.BindingResult;

public class PassWordMissMatchException extends CustomArgumentNotValidException {

    public PassWordMissMatchException(
        BindingResult bindingResult, ErrorCode errorCode) {
        super(bindingResult, errorCode);
    }
}
