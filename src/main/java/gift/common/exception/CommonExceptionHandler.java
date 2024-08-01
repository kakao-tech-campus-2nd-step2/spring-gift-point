package gift.common.exception;

import gift.common.util.ApiResponse;
import gift.product.exception.kakao.KakaoApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(KakaoApiException.class)
    public ResponseEntity<ApiResponse<String>> handleKakaoApiException(KakaoApiException e) {

        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.error(e.getStatus(), e.getMessage()));
    }
}
