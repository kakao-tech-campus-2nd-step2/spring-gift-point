package gift.common.exception;

import gift.common.exception.UserNotFoundException;
import gift.common.exception.ProductAlreadyExistsException;
import gift.common.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "ErrorPage/NotFound";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public String handleProductAlreadyExistsException(ProductAlreadyExistsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "ErrorPage/AlreadyExists";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "ErrorPage/BadRequest";
    }

}
