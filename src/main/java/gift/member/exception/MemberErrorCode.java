package gift.member.exception;

import gift.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND_BY_ID("M001", HttpStatus.NOT_FOUND, "회원 ID에 해당하는 회원을 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS_BY_EMAIL("M002", HttpStatus.BAD_REQUEST, "해당 이메일로 생성하려는 회원이 이미 존재합니다."),

    MEMBER_CREATE_FAILED("M003", HttpStatus.INTERNAL_SERVER_ERROR, "회원 생성에 실패했습니다."),
    MEMBER_UPDATE_FAILED("M004", HttpStatus.INTERNAL_SERVER_ERROR, "회원 정보 수정에 실패했습니다."),
    MEMBER_DELETE_FAILED("M005", HttpStatus.INTERNAL_SERVER_ERROR, "회원 삭제에 실패했습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(String code, HttpStatus httpStatus, String message) {
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
