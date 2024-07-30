package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", 401, HttpStatus.UNAUTHORIZED),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다.", 404, HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("존재하지 않는 카테코리입니다.", 404, HttpStatus.NOT_FOUND),
    OPTION_NOT_FOUND("존재하지 않는 옵션입니다.", 404, HttpStatus.NOT_FOUND),
    WISHLIST_NOT_FOUND("해당 제품이 위시리스트에 없습니다.", 404, HttpStatus.NOT_FOUND),
    UNAUTHORIZED("인증되지 않은 사용자입니다.", 401, HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIAL("잘못된 비빌번호 입니다.", 401, HttpStatus.UNAUTHORIZED),
    DUPLICATE_MEMBER_EMAIL("이미 등록된 이메일입니다.", 409, HttpStatus.CONFLICT),
    DUPLICATE_CATEGORY_NAME("중복된 카테고리 이름입니다.", 409, HttpStatus.CONFLICT),
    DUPLICATE_OPTION_NAME("중복된 옵션 이름입니다.", 409, HttpStatus.CONFLICT),
    MINIMUM_OPTION_REQUIRED("옵션은 최소 한 개 이상입니다.",400, HttpStatus.BAD_REQUEST),
    INSUFFICIENT_QUANTITY("수량이 부족합니다.", 400, HttpStatus.BAD_REQUEST);

    private final String message;
    private final int statusCode;
    private final HttpStatus status;

    ErrorCode(String message, int statusCode, HttpStatus status) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
