package gift.util;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Product ErrorMessage
    INVALID_NAME("상품의 이름은 필수입니다.", HttpStatus.BAD_REQUEST),
    NAME_HAS_RESTRICTED_WORD("상품의 이름에 금지어가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
    NAME_TOO_LONG("상품의 이름은 최대 15자입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PRICE("상품의 가격은 필수입니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_PRICE("상품의 가격은 0보다 커야합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("해당 상품을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),

    // User ErrorMessage
    DUPLICATE_USER("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST),

    // User ErrorMessage
    LOGIN_FAILED("로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),


    // Wishlist ErrorMessage
    WISHLIST_NOT_FOUND("해당 위시리스트를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    WISHLIST_ALREADY_EXISTS("이미 위시리스트가 존재합니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_OPTION_NAME("이미 존재하는 옵션 이름입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_OPTION_NOT_FOUND("해당 상품 옵션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_USER_OWNED("해당 유저의 소유가 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_TYPE("유효하지 않은 사용자 타입입니다.", HttpStatus.BAD_REQUEST),

    OUT_OF_STOCK("재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
