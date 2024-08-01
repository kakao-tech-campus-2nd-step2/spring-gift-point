package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email already exists"),
    INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password and password confirmation do not match"),

    DUPLICATE_CATEGORY_NAME(HttpStatus.CONFLICT, "Category name already exists");

    private HttpStatus status;

    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    protected String getMessage() {
        return message;
    }
}
