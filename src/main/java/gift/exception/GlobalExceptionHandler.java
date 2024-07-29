package gift.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(
        ProductNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
        MethodArgumentNotValidException e, Model model) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        response.put("message", "Validation failed");
        model.addAttribute("errorMessage", response.get("message"));
        return "redirect:/";
    }

    @ExceptionHandler(ForbiddenWordException.class)
    public String handleIllegalArgumentException(
        ForbiddenWordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleNotRegisterdAccount(ForbiddenException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(NonIntegerPriceException.class)
    public String handleNonIntegerPrice(NonIntegerPriceException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "addproductform";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't", name, type,
            value);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}