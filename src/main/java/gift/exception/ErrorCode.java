package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_PRODUCT(HttpStatus.BAD_REQUEST, "ID: %d는 유효하지 않은 상품정보입니다."),
    INVALID_MEMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 멤버정보입니다."),
    INVALID_WISH(HttpStatus.BAD_REQUEST, "ID: %d는 유효하지 않은 위시정보입니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "ID: %d는 유효하지 않은 카테고리입니다."),
    INVALID_OPTION(HttpStatus.BAD_REQUEST, "ID: %d는 유효하지 않은 옵션입니다."),
    ALREADY_EXIST_OPTION(HttpStatus.BAD_REQUEST, "%s는 이미 존재하는 옵션입니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "현재 재고인 %d보다 더 큰 값을 차감할 수 없습니다."),
    DUPLICATE_OPTION(HttpStatus.BAD_REQUEST, "옵션 이름은 중복될 수 없습니다."),
    UNAUTHORIZED_KAKAO(HttpStatus.UNAUTHORIZED, "카카오 사용자 권한이 유효하지 않습니다."),
    NO_OPTIONS(HttpStatus.BAD_REQUEST, "옵션은 최소 하나 이상 필요합니다.");
    private final HttpStatus error;

    private final String message;

    ErrorCode(HttpStatus error, String message) {
        this.error = error;
        this.message = message;
    }

    public HttpStatus getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
