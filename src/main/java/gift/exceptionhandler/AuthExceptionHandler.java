package gift.exceptionhandler;

import gift.exception.FailedLoginException;
import gift.exception.InvalidAccessTokenException;
import gift.member.MemberController;
import gift.wishlist.WishlistController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {WishlistController.class, MemberController.class})
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(FailedLoginException.class)
    public String handleFailedLoginException(FailedLoginException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidAccessTokenException.class)
    public String handleInvalidAccessTokenException(InvalidAccessTokenException exception) {
        return exception.getMessage();
    }
}
