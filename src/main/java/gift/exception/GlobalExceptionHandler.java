package gift.exception;

import gift.util.ResponseDto;
import gift.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseUtil {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidUserInputException.class, DuplicateEmailException.class, DuplicateResourceException.class})
  public ResponseEntity<String> handleInvalidUserInputException(InvalidUserInputException ex) {
    return ResponseUtil.createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<String> handleAuthenticationFailedException(AuthenticationFailedException ex) {
    return ResponseUtil.createResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(RuntimeException.class)
  public String handleAllExceptions(RuntimeException ex, Model model) {
    model.addAttribute("exception", ex);
    return "error";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(QuantityException.class)
  public ResponseEntity<String> handleQuantityException(QuantityException ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
