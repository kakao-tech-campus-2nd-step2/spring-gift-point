package gift.member.exception;

import org.springframework.http.HttpStatus;

public class MemberAlreadyExistsException extends MemberException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "같은 이름의 사용자가 이미 존재합니다.";

    public MemberAlreadyExistsException() {
        super(MESSAGE, HTTP_STATUS);
    }
}
