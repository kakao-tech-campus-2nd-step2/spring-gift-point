package gift.option.exception;

import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OptionErrorCode implements ErrorCode {
    DUPLICATED_OPTION_NAME("O001", HttpStatus.BAD_REQUEST, "상품의 옵션 이름은 중복될 수 없습니다."),
    NOT_ENOUGH_STOCK("O002", HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    OPTION_NOT_FOUND("O003", HttpStatus.NOT_FOUND, "옵션을 찾을 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    OptionErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
