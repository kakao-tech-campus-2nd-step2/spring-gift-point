package gift.exception.order;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundOrderException.class)
    public ErrorResult notFoundExHandler(NotFoundOrderException e) {
        return new ErrorResult("주문 조회 에러", e.getMessage());
    }
}
