package gift.error;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionRestController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
        MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ApiResponse<>(HttpResult.ERROR, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST,
                ErrorResponse.of(bindingResult)));
    }

    @ExceptionHandler(KakaoAuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleKakaoAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ApiResponse<>(HttpResult.ERROR, "카카오 로그인 에러", HttpStatus.BAD_REQUEST,
                ex.getMessage()));
    }

    @ExceptionHandler(NotFoundIdException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundIdException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(HttpResult.ERROR, "아이디 에러", HttpStatus.NOT_FOUND,
                ex.getMessage()));
    }

    @ExceptionHandler(KakaoOrderException.class)
    public ResponseEntity<ApiResponse<String>> handleKakaoOrderException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(HttpResult.ERROR, "카카오 주문하기 에러", HttpStatus.BAD_REQUEST,
                ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(HttpResult.ERROR, "잘못된 요청", HttpStatus.BAD_REQUEST,
                ex.getMessage()));
    }
}
