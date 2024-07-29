package gift.controller;

import gift.exception.AlreadyExistException;
import gift.exception.LogicalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    /*
     * Validation check를 통과하지 못한 경우 발생하는 Exception에 대한 Handling
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    /*
     * 인증 ( 로그인, 유저 정보 ) 에 관련된 Exception에 대한 Handling
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationExceptions(IllegalArgumentException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("user_info", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }
    /*
     * 값을 다시 확인해야 하는 부분에 대한 Handling
     */
    @ExceptionHandler(NoSuchFieldError.class)
    public ResponseEntity<Map<String, String>> handleNullExceptions(NoSuchFieldError e){
        Map<String, String> errors = new HashMap<>();
        String message = (e.getMessage() != null) ? e.getMessage() : "입력 값을 다시 확인해보세요!";
        errors.put("nothing", message);
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
    /*
     * 이미 존재하며 중복되지 말아야 하는 값을 추가할 때에 대한 Handling
     */
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleExistExceptions(AlreadyExistException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("exist", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
    /*
     * 논리적인 오류에 대한 Handling ( 수량 초과 등 )
     */
    @ExceptionHandler(LogicalException.class)
    public ResponseEntity<Map<String, String>> handleLogicalExceptions(LogicalException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("logical", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
