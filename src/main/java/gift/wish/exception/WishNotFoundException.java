package gift.wish.exception;

import org.springframework.http.HttpStatus;

public class WishNotFoundException extends WishException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "id %s와 일치하는 위시리스트를 찾을 수 없습니다.";

    private WishNotFoundException(Long wishId) {
        super(MESSAGE.formatted(wishId.toString()), HttpStatus.NOT_FOUND);
    }

    public static WishNotFoundException of(Long wishId) {
        return new WishNotFoundException(wishId);
    }
}
