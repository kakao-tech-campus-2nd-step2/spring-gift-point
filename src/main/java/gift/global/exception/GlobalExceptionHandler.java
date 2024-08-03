//package gift.global.exception;
//
//import static gift.global.dto.ApiResponseDto.FAILURE;
//
//import gift.global.dto.ApiResponseDto;
//import jakarta.validation.ConstraintViolationException;
//import java.util.NoSuchElementException;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingRequestHeaderException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.server.ResponseStatusException;
//
//// 반환 타입을 ApiResponseDto로 통일시키면서 global하게 바꿨습니다.
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ApiResponseDto<Void> handler(
//        MethodArgumentNotValidException methodArgumentNotValidException) {
//        String message = methodArgumentNotValidException.getFieldError().getDefaultMessage();
//
//        return FAILURE(message);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ApiResponseDto<Void> handler(NoSuchElementException noSuchElementException) {
//        String message = noSuchElementException.getMessage();
//
//        return FAILURE(message);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ApiResponseDto<Void> handler(IllegalArgumentException illegalArgumentException) {
//        String message = illegalArgumentException.getMessage();
//
//        return FAILURE(message);
//    }
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ApiResponseDto<Void> handler(ResponseStatusException responseStatusException) {
//        String message = responseStatusException.getMessage();
//        return FAILURE(message);
//    }
//
//    // Validated에 걸린 경우
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ApiResponseDto<Void> handler(ConstraintViolationException constraintViolationException) {
//        String message = constraintViolationException.getMessage().split(":")[1].trim();
//        return FAILURE(message);
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ApiResponseDto<Void> handler(
//        DataIntegrityViolationException dataIntegrityViolationException) {
//        String message = "제약 조건에 위배됩니다.";
//        return FAILURE(message);
//    }
//
//    @ExceptionHandler(NullPointerException.class)
//    public ApiResponseDto<Void> handler(NullPointerException nullPointerException) {
//        String message = "잘못된 값을 요청하였습니다.";
//        return FAILURE(message);
//    }
//
//    // 헤더가 유실된 경우인데, 보통은 조작을 빨리 해서 토큰이 누락된 경우 예외가 발생합니다.
//    @ExceptionHandler(MissingRequestHeaderException.class)
//    public ApiResponseDto<Void> handler(
//        MissingRequestHeaderException missingRequestHeaderException) {
//        String message = "조작이 너무 빠릅니다.";
//        return FAILURE(message);
//    }
//}
