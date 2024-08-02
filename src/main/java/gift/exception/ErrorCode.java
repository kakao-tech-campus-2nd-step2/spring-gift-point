package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_NAME_SIZE("상품 이름은 15자 이하여야합니다. (공백포함)", HttpStatus.BAD_REQUEST),
    INVALID_NAME_PATTERN("( ), [ ], +, -, &, /, _ 외 특수문자는 사용 불가능합니다.", HttpStatus.BAD_REQUEST),
    KAKAO_NAME_NOT_ALLOWED("상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의해 주세요.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("해당 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("해당 이메일이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("이메일 또는 비밀번호가 잘못되었습니다.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND("해당 사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_REQUEST("유효한 인증 자격 증명이 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PRICE("가격은 음수일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_URL("이미지 URL은 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("이메일은 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("비밀번호는 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_NAME("카테고리는 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_COLOR("Color은 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_OPTION_NAME("옵션은 null이거나 빈 문자열일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("수량은 1개 이상 1억개 미만여야 합니다.", HttpStatus.BAD_REQUEST),
    OPTION_NOT_FOUND("해당 옵션이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_OPTION_NOT_FOUND("해당 상품 옵션이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_OPTION("상품에 동일한 옵션이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_QUANTITY("남은 수량이 부족합니다.", HttpStatus.BAD_REQUEST),
    INVALID_DECREASE_QUANTITY("감소할 수량은 0보다 커야합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("해당 카테고리가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    KAKAO_MESSAGE_SEND_FAILED("카카오 메시지 전송을 실패했습니다.", HttpStatus.BAD_REQUEST),
    KAKAO_AUTH_FAILED("카카오 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_POINTS("포인트가 부족합니다.", HttpStatus.BAD_REQUEST);

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
