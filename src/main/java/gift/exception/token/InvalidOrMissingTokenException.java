package gift.exception.token;

import static gift.exception.ErrorCode.INVALID_OR_MISSING_TOKEN;

import gift.exception.CustomException;

public class InvalidOrMissingTokenException extends CustomException {

    public InvalidOrMissingTokenException() {
        super(INVALID_OR_MISSING_TOKEN);
    }
}
