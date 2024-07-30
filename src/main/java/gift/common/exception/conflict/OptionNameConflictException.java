package gift.common.exception.conflict;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OptionNameConflictException extends CustomException {

    public OptionNameConflictException() {
        super(ErrorCode.OPTION_NAME_CONFLICT, HttpStatus.CONFLICT);
    }

}
