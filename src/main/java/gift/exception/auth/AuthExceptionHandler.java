package gift.exception.auth;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FailAuthenticationException.class)
    public ErrorResult authenticationExHandle(FailAuthenticationException e) {
        return new ErrorResult("인증 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(FailAuthorizationException.class)
    public ErrorResult authorizationExHandle(FailAuthorizationException e) {
        return new ErrorResult("인가 에러", e.getMessage());
    }
}
