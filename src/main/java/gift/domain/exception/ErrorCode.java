package gift.domain.exception;

public enum ErrorCode {

    //Bad Requests
    FIELD_VALIDATION_FAIL("EBR_VF001", -40001),
    OPTION_QUANTITY_OUT_OF_RANGE("EBR_OP001", -40002),
    PRODUCT_OPTIONS_EMPTY("EBR_OP003", -40003),

    //Unauthorized
    TOKEN_NOT_FOUND("EUA_MB001", -40101),
    TOKEN_EXPIRED("EFD_MB003", -40102),
    TOKEN_UNEXPECTED_ERROR("EUA_MB002", -40103),
    TOKEN_STRING_INVALID("EFD_MB004", -40103),

    //Forbidden
    MEMBER_INCORRECT_LOGIN_INFO("EFD_MB001", -40301),
    //TODO: 다른 멤버의 위시를 삭제하는 오류 추가 (-40302)

    //Not Found
    PRODUCT_NOT_FOUND("ENF_PD001", -40401),
    CATEGORY_NOT_FOUND("ENF_CAT001", -40402),
    //TODO: 위시 찾을 수 없는 오류 추가 (-40403)
    OPTION_NOT_FOUND("ENF_OP001", -40404),
    OPTION_NOT_INCLUDED_IN_PRODUCT_OPTIONS("ENF_OP002", -40405),

    //Conflicts
    MEMBER_ALREADY_EXISTS("ECF_MB001", -40901),
    CATEGORY_ALREADY_EXISTS("ECF_CAT001", -40902),
    PRODUCT_ALREADY_EXISTS("ECF_PD001", -40903),
    OPTION_ALREADY_EXISTS_IN_PRODUCT("ECF_OP001", -40904),
    //TODO: 위시리스트에 이미 상품이 존재하는 오류 추가 (-40905)

    //Unused
    OPTION_UPDATE_ACTION_INVALID("EBR_OP002", -400_999),
    OAUTH_VENDOR_ILLEGAL("EBR_OA001", -400_999),
    MEMBER_NOT_ADMIN("EFD_MB002", -403_999),
    PRODUCT_NOT_INCLUDED_IN_WISHLIST("ENF_WS001", -404_999),
    MEMBER_NOT_FOUND("ENF_MB001", -404_999),
    CATEGORY_HAS_PRODUCTS("ECF_CAT002", -409_999);

    private final Integer errorCode;
    private final String errorIdentifier;

    ErrorCode(String errorIdentifier, Integer errorCode) {
        this.errorIdentifier = errorIdentifier;
        this.errorCode = errorCode;
    }

    public String getErrorIdentifier() {
        return errorIdentifier;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

}
