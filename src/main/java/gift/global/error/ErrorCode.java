package gift.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Authentication Error
    AUTHENTICATION_INVALID(HttpStatus.UNAUTHORIZED, "인증이 유효하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "인증에 실패하였습니다. 로그인을 다시 시도해주세요."),
    AUTHENTICATION_EXPIRED(HttpStatus.FORBIDDEN, "인증이 만료되었습니다."),

    // Product Error
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품은 존재하지 않습니다."),

    // Category Error
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리는 존재하지 않습니다."),

    // Option Error
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 옵션은 존재하지 않습니다."),
    OPTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 옵션입니다."),
    OPTION_REMOVE_FAILED(HttpStatus.CONFLICT, "상품은 항상 하나 이상의 옵션이 있어야 합니다."),
    OPTION_QUANTITY_SUBTRACT_FAILED(HttpStatus.CONFLICT, "해당 수량은 뺄 수 없습니다."),

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원 계정입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원 계정입니다."),
    MEMBER_NOT_KAKAO_USER(HttpStatus.CONFLICT, "해당 서비스는 카카오 유저만 이용할 수 있습니다."),
    MEMBER_POINT_NOT_DEDUCTIBLE(HttpStatus.CONFLICT, "포인트가 부족하여 결제할 수 없습니다."),

    // Wish Error
    WISH_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 상품이 위시리스트에 존재합니다."),

    // External API Error
    EXTERNAL_API_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "해당 서비스는 현재 이용할 수 없습니다."),

    // Order Error
    ORDER_POINT_EXCEED_PURCHASE_AMOUNT(HttpStatus.CONFLICT, "포인트가 구매 금액을 초과하여 주문할 수 없습니다.")

    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
