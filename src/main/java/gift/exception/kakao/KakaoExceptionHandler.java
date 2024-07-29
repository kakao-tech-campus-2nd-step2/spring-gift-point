package gift.exception.kakao;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KakaoExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(KakaoMessageException.class)
    public ErrorResult kakaoMessageExHandle(KakaoMessageException e) {
        return new ErrorResult("카카오톡 메시지 전송 에러", e.getMessage());
    }
}
