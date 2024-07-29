package gift.category.exception;

import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND("C001", HttpStatus.NOT_FOUND, "카테고리 ID에 해당하는 카테고리를 찾을 수 없습니다."),
    CATEGORY_ALREADY_EXISTS("C002", HttpStatus.BAD_REQUEST, "생성하려는 카테고리가 이미 존재합니다."),
    CATEGORY_CREATE_FAILED("C003", HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 생성에 실패했습니다."),
    CATEGORY_UPDATE_FAILED("C004", HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 정보 수정에 실패했습니다."),
    CATEGORY_DELETE_FAILED("C005", HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 삭제에 실패했습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    CategoryErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
