package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_TYPE_VALUE("입력 데이터가 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("지원하지 않는 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),

    INTERNAL_SERVER_ERROR("서버 내부 오류",HttpStatus.INTERNAL_SERVER_ERROR),
    //Products
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다.",HttpStatus.NOT_FOUND),
    PRODUCT_NAME_NOT_ALLOWED("상품 이름 규칙에 맞지 않습니다.",HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_DUPLICATED("중복된 상품 이름이 이미 존재합니다.",HttpStatus.CONFLICT),
    PRODUCT_HAVE_NO_OPTION("상품에는 최소 1개의 옵션이 존재해야 합니다.",HttpStatus.BAD_REQUEST),

    //Member
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다",HttpStatus.NOT_FOUND),
    MEMBER_EMAIL_NOT_ALLOWED("회원 ID 규칙에 맞지 않습니다.",HttpStatus.BAD_REQUEST),
    MEMBER_PASSWORD_NOT_ALLOWED("회원 비밀번호 규칙에 맞지 않습니다.",HttpStatus.BAD_REQUEST),
    MEMBER_EMAIL_DUPLICATED("중복된 EMAIL이 이미 존재합니다.",HttpStatus.CONFLICT),
    MEMBER_LOGIN_NOT_ALLOWED("사용자 정보가 일치하지 않습니다.",HttpStatus.BAD_REQUEST),

    //Wishlist
    WISHLIST_NOT_FOUND("존재하지 않는 위시리스트입니다.",HttpStatus.NOT_FOUND),
    WISHLIST_ALREADY_EXISTS("이미 위시리스트에 추가된 상품입니다.",HttpStatus.CONFLICT),

    //Category
    CATEGORY_NOT_FOUND("존재하지 않는 상품 카테고리입니다.",HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_DUPLICATED("이미 존재하는 카테고리 이름입니다.",HttpStatus.CONFLICT),

    //Option
    OPTION_NOT_FOUND("존재하지 않는 상품 옵션입니다.",HttpStatus.NOT_FOUND),
    OPTION_NAME_DUPLICATED("중복된 옵션입니다.",HttpStatus.CONFLICT),
    OPTION_NOT_SUBSTRACT("옵션의 수량이 부족합니다.",HttpStatus.BAD_REQUEST),

    //JWT Token
    TOKEN_INVALID("유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_EXISTS("토큰이 존재하지 않거나 올바르지 않은 형식입니다.",HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;



    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;

    }

    public HttpStatus getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }
}
