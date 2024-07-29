package gift.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InputExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InputException.class})
    public List<ErrorResult> inputExHandle(InputException e) {

        return e.getErrors().stream()
            .map(er -> (new ErrorResult("잘못된 입력", er.getDefaultMessage())))
            .toList();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorResult typeExHandle(HttpMessageNotReadableException e) {
        return new ErrorResult("잘못된 입력", e.getMessage());
    }
}
