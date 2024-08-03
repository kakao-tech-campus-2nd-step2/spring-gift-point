package gift.exception.order;

import gift.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderErrorCode implements BaseErrorCode {
    POINT_USAGE_EXCEEDS_LIMIT(HttpStatus.BAD_REQUEST, "포인트는 상품 가격의 50% 이하로 사용할 수 있습니다."),
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.");
    
    private final HttpStatus httpStatus;
    private final String message;

    OrderErrorCode(HttpStatus httpStatus, String message) {
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
