package gift.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchProductException.class, NoSuchMemberException.class, NoSuchWishedProductException.class,
        NoSuchCategoryException.class, NoSuchOptionException.class})
    public ProblemDetail handleNotFoundException(RuntimeException runtimeException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(runtimeException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        var reasons = methodArgumentNotValidException.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        problemDetail.setProperties(Map.of("object", reasons));
        problemDetail.setDetail(methodArgumentNotValidException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler({AlreadyExistMemberException.class, InvalidPasswordException.class, NoOptionsForProductException.class,
        InsufficientQuantityException.class,  NoKakaoTokenException.class, InvalidKakaoTalkTemplateException.class})
    public ProblemDetail handleRuntimeException(RuntimeException runtimeException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(runtimeException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ProblemDetail handleInvalidAccessTokenException(InvalidAccessTokenException invalidAccessTokenException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setDetail(invalidAccessTokenException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(dataIntegrityViolationException.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvalidKakaoTokenException.class)
    public ProblemDetail handleInvalidKakaoTokenException(InvalidKakaoTokenException invalidKakaoTokenException) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(invalidKakaoTokenException.getStatusCode());
        problemDetail.setDetail(invalidKakaoTokenException.getReason());
        return problemDetail;
    }
}
