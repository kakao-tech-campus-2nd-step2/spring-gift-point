package gift.web.validation.exception.client;

import static gift.web.validation.exception.code.ErrorStatus.INCORRECT_EMAIL;

import gift.web.validation.exception.CustomException;
import gift.web.validation.exception.code.ErrorStatus;

public class IncorrectEmailException extends CustomException {

    private static final String ERROR_MESSAGE = "%s 는 잘못된 이메일입니다.";

    public IncorrectEmailException(String email) {
        super(ERROR_MESSAGE.formatted(email));
    }

    public IncorrectEmailException(String email, Throwable cause) {
        super(ERROR_MESSAGE.formatted(email), cause);
    }

    public IncorrectEmailException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorStatus getErrorStatus() {
        return INCORRECT_EMAIL;
    }

}
