package gift.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),

    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "위시리스트에 존재하지 않는 상품입니다."),
    EXIST_WISH(HttpStatus.BAD_REQUEST, "이미 위시 리스트에 추가된 상품입니다."),

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_CANNOT_DELETE(HttpStatus.BAD_REQUEST, "삭제할 수 없는 카테고리입니다."),

    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 옵션입니다."),
    DUPLICATE_OPTION_NAME(HttpStatus.BAD_REQUEST, "중복된 옵션 이름입니다."),
    NOT_ENOUGH_QUANTITY(HttpStatus.BAD_REQUEST, "현재 재고보다 많이 삭제할 수 없습니다."),

    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 로그인 타입입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
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
