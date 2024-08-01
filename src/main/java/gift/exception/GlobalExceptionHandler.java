package gift.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(
        IllegalArgumentException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(
        NoSuchElementException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(
        DuplicateEmailException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchEmailException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEmailException(
        NoSuchEmailException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(
        BadCredentialsException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.FORBIDDEN.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(
        UnauthorizedException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateWishItemException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateWishItemException(
        DuplicateWishItemException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchWishException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchWishException(
        NoSuchWishException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchOptionException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchOptionException(
        NoSuchOptionException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<ExceptionResponse> handleInsufficientQuantityException(
        InsufficientQuantityException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.CONFLICT.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LastOptionDeleteException.class)
    public ResponseEntity<ExceptionResponse> handleLastOptionDeleteException(
        LastOptionDeleteException e) {
        ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
            e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
