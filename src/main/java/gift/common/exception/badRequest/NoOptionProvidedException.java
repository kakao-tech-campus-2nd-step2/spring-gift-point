package gift.common.exception.badRequest;

import gift.common.exception.CustomException;
import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoOptionProvidedException extends CustomException {

    public NoOptionProvidedException() {
        super(ErrorCode.NO_OPTION_PROVIDED, HttpStatus.BAD_REQUEST);
    }

}
