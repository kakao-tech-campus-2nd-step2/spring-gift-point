package gift.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NameExceptionHandler extends RuntimeException {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void ResourceNotFoundException(NoSuchElementException e,
        HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleProductNameException(IllegalArgumentException e,
        HttpServletResponse response) throws IOException {
        response.sendRedirect("redirect:/product/add/form");
    }

}
