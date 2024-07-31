package gift.exception;

import gift.dto.ErrorResponseDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponseDto> handleLoginException(LoginException ex) {
        Map<String, String> validationErrors = new HashMap<>();

        // 필요할 경우 validation 오류 메시지를 추가
        if (ex.getValidationErrors() != null && !ex.getValidationErrors().isEmpty()) {
            ex.getValidationErrors().forEach((fieldName, errorMessage) -> {
                validationErrors.put(fieldName, errorMessage); // 필드 이름과 오류 메시지를 맵에 추가
            });
        }

        // ErrorResponseDto 생성
        final ErrorResponseDto response = new ErrorResponseDto(
            ex.getMessage(), // 예외 메시지
            ex.getHttpStatus().value(), // HTTP 상태 코드
            validationErrors // validation 오류 메시지
        );

        return new ResponseEntity<>(response, ex.getHttpStatus()); // 상태 코드와 함께 응답 반환
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();

        // 오류가 있을 경우 필드 오류 정보 수집
        if (ex.getBindingResult().hasErrors()) {
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField(); // 필드 이름
                String errorMessage = error.getDefaultMessage(); // 오류 메시지
                validationErrors.put(fieldName, errorMessage);
            });
        }

        // 오류가 없을 경우 빈 맵을 전달
        final ErrorResponseDto response = new ErrorResponseDto(
            "입력 데이터의 유효성을 검사하던 중 문제가 발생했습니다.",  HttpStatus.BAD_REQUEST.value(),
            validationErrors // 오류가 없으면 빈 맵
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity AuthException(AuthException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity jwtException(TokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity businessException(BusinessException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}