package gift.global;

import gift.domain.member.exception.MemberAuthorizationException;
import gift.domain.option.exception.OptionValidException;
import gift.global.exception.DuplicateException;
import gift.global.exception.NotFoundException;
import gift.kakaoApi.exception.KakaoApiException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MemberAuthorizationException.class)
    public ResponseEntity<String> handleMemberException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleNameException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errorMessages.add(errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorMessages);
    }

    @ExceptionHandler(OptionValidException.class)
    public ResponseEntity<String> handleOptionValidException(OptionValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(KakaoApiException.class)
    public ResponseEntity<String> handleKakaoApiException(KakaoApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }
}
