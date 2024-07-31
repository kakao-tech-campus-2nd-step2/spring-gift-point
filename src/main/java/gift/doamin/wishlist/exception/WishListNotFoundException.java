package gift.doamin.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WishListNotFoundException extends ResponseStatusException {

    public WishListNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
