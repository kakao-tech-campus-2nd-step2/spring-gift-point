package gift.exception;

public class ErrorMessage {
    public static final String PRODUCT_MISSING_FIELDS = "상품의 이름, 가격, 설명을 모두 입력해야합니다.";
    public static final String PRODUCT_NOT_FOUND = "일치하는 상품이 없습니다.";
    public static final String INTERNAL_SERVER_ERROR = "서버에 문제가 발생했습니다.";
    public static final String MEMBER_EMAIL_ALREADY_EXISTS = "이메일이 이미 존재합니다.";
    public static final String MEMBER_NOT_FOUND = "일치하는 회원이 없습니다.";
    public static final String INVALID_LOGIN_CREDENTIALS = "일치하는 이메일이 없거나 비밀번호가 틀렸습니다.";
    public static final String EMAIL_NOT_FOUND = "일치하는 이메일이 없습니다.";
}
