package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class WishNoSuchException extends WishServiceException{

    public WishNoSuchException() {
        super("존재하지않는 wishlist 입니다.", HttpStatus.NOT_FOUND);
    }
}
