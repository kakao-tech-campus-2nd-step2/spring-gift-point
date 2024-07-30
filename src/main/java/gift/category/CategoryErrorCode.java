package gift.category;

import org.springframework.http.HttpStatus;

public enum CategoryErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Category 를 찾을 수 없습니다."),
    CAN_NOT_DELETE(HttpStatus.BAD_REQUEST, "이 Category 를 사용하는 Product 가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CategoryErrorCode(HttpStatus httpStatus, String message) {
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
