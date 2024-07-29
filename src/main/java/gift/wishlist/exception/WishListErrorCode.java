package gift.wishlist.exception;

import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum WishListErrorCode implements ErrorCode {
    // 위시 리스트 추가, 수정, 삭제
    WISH_LIST_ADD_FAILED("W001", HttpStatus.INTERNAL_SERVER_ERROR, "위시 리스트 추가에 실패했습니다."),
    WISH_LIST_UPDATE_FAILED("W002", HttpStatus.INTERNAL_SERVER_ERROR, "위시 리스트 수정에 실패했습니다."),
    WISH_LIST_DELETE_FAILED("W003", HttpStatus.INTERNAL_SERVER_ERROR, "위시 리스트 삭제에 실패했습니다."),

    // 위시 리스트 조회
    WISH_LIST_NOT_FOUND("W004", HttpStatus.NOT_FOUND, "위시 리스트를 찾을 수 없습니다.")
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    WishListErrorCode(String code, HttpStatus httpStatus, String message) {
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
