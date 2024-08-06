package gift.common.exception;

public enum ErrorCode {
    // 400 BAD REQUEST
    REQUEST_VALIDATION(-40001, "요청 시 만족해야하는 제약 조건을 위반함"),
    NO_OPTION_PROVIDED(-40002, "상품의 옵션이 하나 이상이 아님"),
    OVER_STOCK_QUANTITY(-40003, "주문 생성 시 재고보다 많은 수량을 입력함"),
    INVALID_POINT_AMOUNT(-40004,"포인트 사용량이 잔여 포인트 또는 주문 금액보다 큼"),

    // 401 UNAUTHORIZED
    TOKEN_NOT_FOUND(-40101, "토큰을 찾을 수 없음"),
    TOKEN_EXPIRED(-40102, "토큰이 만료됨"),
    TOKEN_ERROR(-40103, "토큰과 관련된 알 수 없는 오류"),

    // 403 FORBIDDEN
    INVALID_CREDENTIALS(-40301, "로그인 시 아이디 또는 비밀번호가 틀림"),
    UNAUTHORIZED_WISH_DELETION(-40302, "다른 멤버의 위시를 삭제하려고 함"),

    // 404 NOT FOUND
    PRODUCT_NOT_FOUND(-40401, "상품을 찾을 수 없음"),
    CATEGORY_NOT_FOUND(-40402, "카테고리를 찾을 수 없음"),
    WISH_NOT_FOUND(-40403, "위시를 찾을 수 없음"),
    OPTION_NOT_FOUND(-40404, "옵션을 찾을 수 없음"),
    INVALID_PRODUCT_OPTION(-40405, "옵션이 존재하나 상품 옵션에 속하지 않음"),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(-40901, "주어진 이메일로 이미 회원가입이 되어있음 (이미 존재하는 회원)"),
    CATEGORY_NAME_CONFLICT(-40902, "카테고리 추가/수정 시 이름이 기존 카테고리와 중복됨"),
    PRODUCT_ALREADY_EXISTS(-40903, "상품 등록/수정 시 기존 상품으로 이미 존재함"),
    OPTION_NAME_CONFLICT(-40904, "상품 옵션에 이미 이름이 중복되는 옵션이 있음"),
    WISH_ALREADY_EXISTS(-40905, "위시리스트에 이미 상품이 존재함");

    private final int code;
    private final String defaultMessage;
    private String customMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getMessage() {
        return customMessage != null ? customMessage : defaultMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
