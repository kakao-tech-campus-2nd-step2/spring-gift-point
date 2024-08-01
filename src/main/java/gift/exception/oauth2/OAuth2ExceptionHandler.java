package gift.exception.oauth2;

import gift.exception.ErrorResult;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OAuth2ExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(OAuth2Exception.class)
    public ErrorResult authExHandle(OAuth2Exception e) {
        return new ErrorResult("인가코드 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(OAuth2TokenException.class)
    public ErrorResult tokenExHandle(OAuth2TokenException e) {
        return new ErrorResult("토큰 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler({TimeoutException.class})
    public ErrorResult timeoutExHandle() {
        return new ErrorResult("타임아웃 에러", "시간 초과로 요청을 처리하지 못하였습니다.");
    }
}
