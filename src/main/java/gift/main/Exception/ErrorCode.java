package gift.main.Exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EMPTY_EMAIL(HttpStatus.BAD_REQUEST, "Please fill in the email"),
    EMPTY_NAME(HttpStatus.BAD_REQUEST, "Please fill in the name"),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, "Please fill in the password"),

    // Authentication related
    NO_TOKEN(HttpStatus.UNAUTHORIZED, "Token is empty"),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "Token not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Authentication failed"),
    NO_PERMISSION(HttpStatus.UNAUTHORIZED, "No permission for this page"),
    NO_PERMISSION_PRODUCT(HttpStatus.UNAUTHORIZED, "No permission for this product"),

    // Login
    ALREADY_EMAIL(HttpStatus.BAD_REQUEST, "Email already exists"),
    ERROR_LOGIN(HttpStatus.BAD_REQUEST, "Please enter the email and password correctly"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "User not found with this email and password"),

    // Product related
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "Category not found"),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "Product not found"),
    ALREADY_EXISTING_WISH_LIST(HttpStatus.BAD_REQUEST, "Product is already in the wish list"),
    ALREADY_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "Category name already exists"),

    // Category related
    ALREADY_CATEGORY_UNI_NUMBER(HttpStatus.BAD_REQUEST, "Unique number already exists"),
    EXISTS_PRODUCT(HttpStatus.BAD_REQUEST, "Cannot delete because the product exists"),

    // Option related
    EMPTY_OPTION(HttpStatus.BAD_REQUEST, "Option is empty"),
    NOT_FOUND_OPTION(HttpStatus.BAD_REQUEST, "Option does not exist"),
    CANNOT_DELETE_OPTION(HttpStatus.BAD_REQUEST, "At least one option must exist"),
    ALREADY_EXISTS_OPTION_NAME(HttpStatus.BAD_REQUEST, "Option name already exists"),
    ALREADY_EXISTS_OPTION_NUM(HttpStatus.BAD_REQUEST, "Option number already exists"),
    DUPLICATE_OPTION(HttpStatus.BAD_REQUEST, "Duplicate options in the request"),
    INVALID_OPTION_NAME_LENGTH(HttpStatus.BAD_REQUEST, "Option name length must be under 50 characters"),
    INVALID_OPTION_NAME_CHARACTERS(HttpStatus.BAD_REQUEST, "Invalid special characters in option name"),
    INVALID_OPTION_QUANTITY(HttpStatus.BAD_REQUEST, "Quantity must be at least one and less than one hundred million"),

    ;
    //이메일과 비밀번호 코드가 401이 아닌 400인 이유: 코드를 보고 해당 유저가 있다고 판단할 것 같아서


    private final HttpStatus httpStatus;
    private final String errorMessage;


    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
