package gift.product;

import org.springframework.http.HttpStatus;

public enum ProductErrorCode {
    HAS_KAKAO_WORD(HttpStatus.BAD_REQUEST, "\"카카오\"가 포함된 문구는 담당자와 협의 후 사용할 수 있습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Product 가 발견되지 않았습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ProductErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
