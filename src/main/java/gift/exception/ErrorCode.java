package gift.exception;

import org.springframework.http.HttpStatus;

import static gift.constant.ErrorMessage.*;

public enum ErrorCode {

    LENGTH_ERROR(400, HttpStatus.BAD_REQUEST, LENGTH_ERROR_MSG),
    SPECIAL_CHAR_ERROR(400, HttpStatus.BAD_REQUEST, SPECIAL_CHAR_ERROR_MSG),
    KAKAO_CONTAIN_ERROR(400, HttpStatus.BAD_REQUEST, KAKAO_CONTAIN_ERROR_MSG),
    DATA_NOT_FOUND(404, HttpStatus.NOT_FOUND, DATA_NOT_FOUND_ERROR_MSG),
    INVALID_QUANTITY_ERROR(404, HttpStatus.BAD_REQUEST, INVALID_QUANTITY_ERROR_MSG),
    DUPLICATE_OPTION_NAME_ERROR(409, HttpStatus.CONFLICT, DUPLICATE_OPTION_NAME_MSG),
    INVALID_AMOUNT_ERROR(400, HttpStatus.BAD_REQUEST, INVALID_AMOUNT_ERROR_MSG),
    KAKAO_LOGIN_FAILED_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, KAKAO_LOGIN_FAILED_ERROR_MSG),
    INVALID_LOGIN_TYPE(419, HttpStatus.CONFLICT, ALREADY_REGISTERED_ERROR_MSG),
    SEND_MSG_FAILED_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, SEND_MSG_FAILED_ERROR_MSG);
    private int status;
    private HttpStatus httpStatus;
    private String message;

    ErrorCode(int status, HttpStatus httpStatus, String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
