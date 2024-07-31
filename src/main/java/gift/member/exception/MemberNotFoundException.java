package gift.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MemberException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "일치하는 사용자를 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE, HTTP_STATUS);
    }
}
