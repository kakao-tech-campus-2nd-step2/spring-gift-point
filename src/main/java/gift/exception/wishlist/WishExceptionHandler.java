package gift.exception.wishlist;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WishExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WishException.class})
    public ErrorResult duplicatedWish(WishException e) {
        return new ErrorResult("위시 리스트 에러", e.getMessage());
    }
}
