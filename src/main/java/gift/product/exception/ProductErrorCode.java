package gift.product.exception;

import gift.globalException.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum ProductErrorCode implements BaseErrorCode {
    PRODUCT_MUST_HAVE_OPTION(HttpStatus.BAD_REQUEST, "상품은 하나 이상의 옵션을 가지고 있어야 합니다."),
    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ProductErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
