package gift.common.exception.notFound;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OptionNotFoundException extends CustomException {

    public OptionNotFoundException() {
        super(ErrorCode.OPTION_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
