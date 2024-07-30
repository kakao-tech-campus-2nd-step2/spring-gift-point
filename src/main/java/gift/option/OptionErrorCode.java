package gift.option;

import org.springframework.http.HttpStatus;

public enum OptionErrorCode {
    NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "Option name 이 중복되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Option 을 찾을 수 없습니다."),
    OPTION_COUNT_ONE(HttpStatus.BAD_REQUEST, "Option 이 1개뿐입니다."),
    NOT_ENOUGH_QUANTITY(HttpStatus.BAD_REQUEST, "Option 수량이 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    OptionErrorCode(HttpStatus httpStatus, String message) {
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
