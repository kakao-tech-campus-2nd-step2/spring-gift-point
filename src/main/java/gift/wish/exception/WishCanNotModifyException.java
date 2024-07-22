package gift.wish.exception;

import org.springframework.http.HttpStatus;

public class WishCanNotModifyException extends WishException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "위시리스트의 상품을 수정할 수 없습니다.";

    public WishCanNotModifyException() {
        super(MESSAGE, HTTP_STATUS);
    }
}
