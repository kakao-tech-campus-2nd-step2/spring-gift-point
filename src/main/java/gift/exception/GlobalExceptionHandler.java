package gift.exception;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.ResponseDTO;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.EmailAlreadyHereException;
import gift.util.ResponseEntityUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseEntityUtil.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("\n");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getDefaultMessage()).append("\n");
        }
        return new ResponseEntity<>(new ResponseDTO(true, errorMessage.toString().trim()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("\n");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append("\n");
        }
        return new ResponseEntity<>(new ResponseDTO(true, errorMessage.toString().trim()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(
            HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ResponseDTO(true, "가격이나 개수는 숫자로 입력해야 합니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(
            MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(new ResponseDTO(true, "가격이나 개수는 숫자로 입력해야 합니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyHereException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(EmailAlreadyHereException ex) {
        return new ResponseEntity<>(new ResponseDTO(true, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(EmptyResultDataAccessException ex) {
        return new ResponseEntity<>(new ResponseDTO(true, "그러한 ID를 가진 상품을 찾지 못하였습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(BadRequestException ex) {
        return new ResponseEntity<>(new ResponseDTO(true, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseDTO(true, ResponseMsgConstants.CRITICAL_ERROR_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
