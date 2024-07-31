package gift.member.exception;

import org.springframework.http.HttpStatus;

public class MemberAuthorizedErrorException extends MemberException {
    private final static String MESSAGE = "로그인 정보가 잘못되었습니다. 다시 로그인해주세요";
    private final static HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    public MemberAuthorizedErrorException() {
        super(MESSAGE, HTTP_STATUS);
    }
}
