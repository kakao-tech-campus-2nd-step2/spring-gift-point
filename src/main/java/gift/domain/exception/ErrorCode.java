package gift.domain.exception;

import gift.global.WebConfig.Constants.Domain.Option;

public enum ErrorCode {

    //Bad Requests
    FIELD_VALIDATION_FAIL("EBR_VF001", -40001, ""),
    OPTION_QUANTITY_OUT_OF_RANGE("EBR_OP001", -40002,
        "The option stock quantity must be greater than or equal to "
        + Option.QUANTITY_RANGE_MIN
        + " and less than "
        + Option.QUANTITY_RANGE_MAX
        + "."),
    PRODUCT_OPTIONS_EMPTY("EBR_OP003", -40003, "A product must have at least one option."),
    SORT_TYPE_ILLEGAL("EBR_VF002", -40004, "The sort option was illegal."),
    FIELD_NAME_ILLEGAL("EBR_VF003", -40001, "The field name was illegal."),

    //Unauthorized
    TOKEN_NOT_FOUND("EUA_MB001", -40101, "토큰이 존재하지 않습니다. 발급이 필요합니다."),
    TOKEN_EXPIRED("EFD_MB003", -40102, "토큰이 만료되었습니다. 재발급이 필요합니다."),
    TOKEN_UNEXPECTED_ERROR("EUA_MB002", -40103, "알 수 없는 오류가 발생했습니다. 토큰 재발급이 필요합니다."),
    TOKEN_STRING_INVALID("EFD_MB004", -40103, "토큰 문자열이 올바르지 않습니다."),

    //Forbidden
    MEMBER_INCORRECT_LOGIN_INFO("EFD_MB001", -40301, "Incorrect your email or password. Try again."),
    OTHER_MEMBERS_WISH_DELETION("EFD_WS001", -40302, "Deleting other members' wishes is prohibited."),

    //Not Found
    PRODUCT_NOT_FOUND("ENF_PD001", -40401, "The product was not found."),
    CATEGORY_NOT_FOUND("ENF_CAT001", -40402, "The category was not found."),
    WISH_NOT_FOUND("ENF_WS001", -40303, "The wish was not found."),
    OPTION_NOT_FOUND("ENF_OP001", -40404, "The option was not found."),
    OPTION_NOT_INCLUDED_IN_PRODUCT_OPTIONS("ENF_OP002", -40405, "The option is not included in the product's options."),

    //Conflicts
    MEMBER_ALREADY_EXISTS("ECF_MB001", -40901, "Your email already registered. Retry with other one."),
    CATEGORY_ALREADY_EXISTS("ECF_CAT001", -40902, "This category name already exists. Try other one."),
    PRODUCT_ALREADY_EXISTS("ECF_PD001", -40903, "The product already exists."),
    OPTION_ALREADY_EXISTS_IN_PRODUCT("ECF_OP001", -40904, "The options already exists in product."),
    PRODUCT_ALREADY_EXISTS_IN_WISHLIST("ECF_WS001", -40905, "The product already exists in member's wishlist."),

    //Unused
    OPTION_UPDATE_ACTION_INVALID("EBR_OP002", -400_999, "The option update request's action was invalid. Available actions: " + Option.QuantityUpdateAction.toList()),
    OAUTH_VENDOR_ILLEGAL("EBR_OA001", -400_999, "Oauth 공급자가 잘못되었습니다. 관리자에게 문의하세요."),
    MEMBER_NOT_ADMIN("EFD_MB002", -403_999, "어드민 권한이 없습니다."),
    MEMBER_NOT_FOUND("ENF_MB001", -404_999, "member"),
    CATEGORY_HAS_PRODUCTS("ECF_CAT002", -409_999, "This category cannot be deleted because some products are included in it.");

    private final Integer errorCode;
    private final String errorIdentifier;
    private final String errorMessage;

    ErrorCode(String errorIdentifier, Integer errorCode, String errorMessage) {
        this.errorIdentifier = errorIdentifier;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorIdentifier() {
        return errorIdentifier;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
