package gift.exception.user;

import gift.exception.BaseErrorCode;
import gift.exception.CustomException;

public class OauthCustomException extends CustomException {
    public OauthCustomException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public static class FailedToSendMessageException extends OauthCustomException {
        public FailedToSendMessageException() {
            super(OauthErrorCode.FAILED_TO_SEND_MESSAGE);
        }
    }
}
