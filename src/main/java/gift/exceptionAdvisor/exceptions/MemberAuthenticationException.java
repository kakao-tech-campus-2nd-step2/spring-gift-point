package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class MemberAuthenticationException extends MemberServiceException {

    public MemberAuthenticationException() {
        super("옳지 못한 인증 시도 입니다.", HttpStatus.UNAUTHORIZED);
    }

    public MemberAuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
