package gift.global.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Global
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG001", "Unexpected Error"),
    ACCESS_TOKEN_NOT_EXISTS_ERROR(HttpStatus.BAD_REQUEST, "EG002", "Access Token Not Exists Error"),
    JWT_ERROR(HttpStatus.UNAUTHORIZED, "EG003", "JWT token is not valid"),

    // Member
    MEMBER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EM001", "Member Not Found Error"),
    DUPLICATE_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "EM002", "Duplicate Email Error"),
    DUPLICATE_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "EM003", "Duplicate Nickname Error"),

    // Product
    PRODUCT_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EP001", "Product Not Found Error"),
    PRODUCT_NAME_LENGTH_ERROR(HttpStatus.BAD_REQUEST, "EP002", "상품 이름은 공백이 아니고, 15자 이하여야 합니다"),
    PRODUCT_NAME_PATTER_ERROR(HttpStatus.BAD_REQUEST, "EP003", "( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용 불가능합니다"),
    PRODUCT_NAME_CONTAINS_ERROR(HttpStatus.BAD_REQUEST, "EP004", "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다"),
    PRODUCT_PRICE_OUT_OF_RANGE_ERROR(HttpStatus.BAD_REQUEST, "EP005", "가격은 0보다 큰 값만 가능합니다"),
    PRODUCT_COUNT_OUT_OF_RANGE_ERROR(HttpStatus.BAD_REQUEST, "EP006", "개수는 0보다 큰 값만 가능합니다"),

    // Category
    CATEGORY_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EC001", "Category Not Found Error"),


    // Wish
    WISH_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EW001", "Wish Not Found Error"),

    // Option
    OPTION_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EO001", "Option Not Found Error"),
    OPTION_NAME_LENGTH_ERROR(HttpStatus.BAD_REQUEST, "EO002", "옵션 이름은 공백이 아니고, 50자 이하여야 합니다"),
    OPTION_NAME_PATTER_ERROR(HttpStatus.BAD_REQUEST, "EO003", "( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용 불가능합니다"),
    OPTION_COUNT_OUTBOUND_ERROR(HttpStatus.BAD_REQUEST, "EO004", "옵션은 1 ~ 1억개 여야 합니다"),
    OPTION_NOT_ENOUGH_ERROR(HttpStatus.BAD_REQUEST, "EO005", "Option Not Enough Error"),
    DUPLICATE_OPTION_NAME_ERROR(HttpStatus.BAD_REQUEST, "EO006", "Duplicate Option Name Not Error"),

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + " : " + message;
    }
}
