package gift.exception.member;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResult joinExHandle(DuplicateEmailException e) {
        return new ErrorResult("회원가입 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundMemberException.class})
    public ErrorResult notFoundUserExHandle(NotFoundMemberException e) {
        return new ErrorResult("로그인 에러", e.getMessage());
    }

}
