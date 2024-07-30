package gift.wish;

import org.springframework.http.HttpStatus;

public enum WishErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Wish 가 발견되지 않았습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    WishErrorCode(HttpStatus httpStatus, String message) {
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
