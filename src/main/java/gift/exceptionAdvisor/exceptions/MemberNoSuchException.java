package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class MemberNoSuchException extends MemberServiceException {

    public MemberNoSuchException() {
        super("존재하지않는 회원입니다", HttpStatus.NOT_FOUND);
    }
}
