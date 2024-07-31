package gift.exception;

import gift.exception.category.DuplicateCategoryNameException;
import gift.exception.member.DuplicatedEmailException;
import gift.exception.member.InvalidAccountException;
import gift.exception.member.PasswordMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    protected ResponseEntity<String> HandleDuplicatedEmail(DuplicatedEmailException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    protected ResponseEntity<String> HandlePasswordMismatch(PasswordMismatchException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(InvalidAccountException.class)
    protected ResponseEntity<String> HandleInvalidAccount(InvalidAccountException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateCategoryNameException.class)
    protected ResponseEntity<String> HandleDuplicateCategoryName(
        DuplicateCategoryNameException e
    ) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<String> HandleHttpClientError(
        HttpClientErrorException e
    ) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
