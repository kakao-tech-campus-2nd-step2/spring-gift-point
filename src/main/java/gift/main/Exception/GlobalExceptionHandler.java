package gift.main.Exception;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Model model, Exception e) {
        model.addAttribute("error", e.getMessage());
        String refererUrl = " http://localhost:8080/spring-gift/";
        model.addAttribute("refererUrl", refererUrl);
        return "error/error";
    }

    @ResponseBody
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleTokenException(Model model, SignatureException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errorMessage", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseBody);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidArgument(IllegalArgumentException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errorMessage", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleUserException(CustomException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationFailure(MethodArgumentNotValidException e) {
        System.out.println("e.getMessage() = " + e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        StringBuilder errorMessageBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            if (errorMessageBuilder.length() > 0) {
                errorMessageBuilder.append(", ");
            }
            errorMessageBuilder.append(fieldError.getDefaultMessage());
        }

        String errorMessage = errorMessageBuilder.toString();
        return ResponseEntity.badRequest().body(errorMessage);
    }


}
