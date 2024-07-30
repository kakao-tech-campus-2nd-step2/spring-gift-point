package gift.common.exception;

import gift.member.MemberErrorCode;
import org.springframework.http.HttpStatus;

public class MemberException extends RuntimeException {

    private final MemberErrorCode memberErrorCode;
    private final HttpStatus httpStatus;

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.getMessage());
        this.memberErrorCode = memberErrorCode;
        this.httpStatus = memberErrorCode.getHttpStatus();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
