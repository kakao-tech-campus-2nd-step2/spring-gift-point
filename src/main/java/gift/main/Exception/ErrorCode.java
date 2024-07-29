package gift.main.Exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //회원가입시
    EMPTY_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 채워주세요"),
    EMPTY_NAME(HttpStatus.BAD_REQUEST, "이름을 채워주세요"),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 채워주세요"),

    //인증관련
    NO_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 비어있습니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    NO_PERMISSION(HttpStatus.UNAUTHORIZED, "해당 페이지에 권합이 없습니다."),
    NO_PERMISSION_PRODUCT(HttpStatus.UNAUTHORIZED, "해당 상품에 권합이 없습니다."),

    //로그인
    ALREADY_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ERROR_LOGIN(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 정확히 입력해주세요"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 정확히 입력해주세요"),

    //제품관련
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당 제품을 찾을 수 없습니다."),
    ALREADY_EXISTING_WISH_LIST(HttpStatus.BAD_REQUEST, "해당 제품은 이미 담겨있습니다."),
    ALREADY_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 이름입니다."),

    //카테고리 관련
    ALREADY_CATEGORY_UNI_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 고유번호입니다."),
    EXISTS_PRODUCT(HttpStatus.BAD_REQUEST, "프로덕트가 존재하기 때문에 삭제할 수 없습니다."),

    //옵션관련
    EMPTY_OPTION(HttpStatus.BAD_REQUEST, "옵션이 비어 있습니다."),
    NOT_FOUND_OPTION(HttpStatus.BAD_REQUEST, "옵션이 존재하지 않습니다."),
    CANNOT_DELETED_OPTION(HttpStatus.BAD_REQUEST, "옵션은 최소 하나 이상 존재해야합니다."),
    ALREADY_EXISTS_OPTION_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 이름입니다."),
    ALREADY_EXISTS_OPTION_NUM(HttpStatus.BAD_REQUEST, "이미 존재하는 번호입니다."),
    DUPLICATE_OPTION(HttpStatus.BAD_REQUEST, "요청내 옵션들이 중복됩니다."),
    INVALID_OPTION_NAME_LENGT(HttpStatus.BAD_REQUEST, "옵셩명 길이는 최대 50자입니다."),
    INVALID_OPTION_NAME_CHARACTERS(HttpStatus.BAD_REQUEST, "사용할 수 없는 특수문자가 있습니다."),
    INVALID_OPTION_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 일억개 미만 한 개 이상으로 가능합니다."),

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
