package gift.constants;

public class ErrorMessage {

    public static final String PRODUCT_ALREADY_EXISTS_MSG = "이미 존재하는 상품입니다.";
    public static final String PRODUCT_NOT_EXISTS_MSG = "존재하지 않는 상품입니다.";
    public static final String ERROR_FIELD_NOT_EXISTS_MSG = "에러 필드가 존재하지 않습니다.";

    public static final String PRODUCT_NAME_VALID_NOT_BLANK_MSG = "상품명에는 빈 값을 입력할 수 없습니다.";
    public static final String PRODUCT_NAME_VALID_SIZE_MSG = "상품명은 공백 포함 최대 15자까지 입력 가능합니다.";
    public static final String PRODUCT_NAME_VALID_CHAR_MSG = "상품명에는 특수 문자 (,),[,],+,-,&,/,_ 만 허용됩니다.";
    public static final String PRODUCT_NAME_VALID_KAKAO_MSG = "\"카카오\" 문구를 사용하시려면 담당 MD와 협의해주세요.";

    public static final String MEMBER_EMAIL_NOT_BLANK_MSG = "이메일을 입력해주세요.";
    public static final String MEMBER_PASSWORD_NOT_BLANK_MSG = "비밀번호를 입력해주세요.";
    public static final String MEMBER_NOT_EMAIL_FORMAT_MSG = "이메일 형식이 아닙니다.";
    public static final String MEMBER_PASSWORD_INVALID_LENGTH_MSG = "비밀번호는 15자까지 입력 가능합니다.";
    public static final String MEMBER_PASSWORD_INVALID_PATTERN_MSG = "비밀번호는 영문 대소문자, 숫자만 입력 가능합니다.";

    public static final String EMAIL_ALREADY_EXISTS_MSG = "이미 존재하는 회원 이메일입니다.";
    public static final String MEMBER_NOT_EXISTS_MSG = "존재하지 않는 회원입니다.";
    public static final String INVALID_PASSWORD_MSG = "비밀번호가 일치하지 않습니다.";
    public static final String NOT_LOGGED_IN_MSG = "로그인 후에 다시 시도해주세요.";
    public static final String WISHLIST_ALREADY_EXISTS_MSG = "이미 위시 리스트에 추가된 상품입니다.";
    public static final String WISHLIST_NOT_EXISTS_MSG = "위시 리스트에 존재하지 않는 상품입니다.";

    public static final String CATEGORY_NOT_EXISTS_MSG = "존재하지 않는 카테고리입니다.";
    public static final String CATEGORY_ALREADY_EXISTS_MSG = "이미 존재하는 카테고리입니다.";
    public static final String CATEGORY_NAME_NOT_BLANK_MSG = "카테고리명을 입력해주세요.";
    public static final String CATEGORY_NAME_INVALID_LENGTH_MSG = "카테고리명은 공백 포함 최대 15자까지 입력 가능합니다.";
    public static final String CATEGORY_MUST_BE_SELECTED = "카테고리를 지정해주세요.";

    public static final String NULL_POINTER_EXCEPTION_MSG = "객체가 null 입니다.";

    public static final String OPTION_NOT_EXISTS_MSG = "존재하지 않는 옵션입니다.";
    public static final String OPTION_NAME_ALREADY_EXISTS_MSG = "이미 존재하는 옵션입니다";
    public static final String OPTION_NAME_DUPLICATE_MSG = "옵션명은 중복되면 안 됩니다.";
    public static final String OPTION_NAME_NOT_BLANK_MSG = "옵션명을 입력해주세요";
    public static final String OPTION_NAME_INVALID_LENGTH_MSG = "옵션명은 공백 포함 최대 50자까지 입력 가능합니다.";
    public static final String OPTION_NAME_INVALID_PATTERN_MSG = "옵션명은 한글,영문,숫자와 특수 문자 (,),[,],+,-,&,,_ 만 입력 가능합니다.";
    public static final String OPTION_QUANTITY_INVALID_MSG = "옵션 수량은 최소 1개 최대 1억개 미만이어야 합니다.";
    public static final String OPTION_MUST_MORE_THAN_ZERO = "옵션은 반드시 1개 이상 있어야 합니다.";
    public static final String OPTION_QUANTITY_FEWER_MSG = "옵션의 수량이 부족합니다.";

    public static final String KAKAO_BAD_REQUEST_MSG = "카카오 로그인 파라미터가 적절하지 않습니다.";
    public static final String KAKAO_UNAUTHORIZED_MSG = "카카오 로그인 인증 정보가 유효하지 않습니다.";
    public static final String KAKAO_FORBIDDEN_MSG = "허가되지 않은 접근입니다.";
    public static final String KAKAO_NO_SUCH_TOKEN_MSG = "카카오 인증 정보가 없습니다. 다시 로그인해주세요.";
    public static final String KAKAO_INTERNAL_SERVER_ERROR_MSG = "예상치 못한 시스템 오류가 발생했습니다.";
    public static final String KAKAO_BAD_GATEWAY_MSG = "Bad Gateway 시스템 오류가 발생했습니다.";
    public static final String KAKAO_SERVICE_UNAVAILABLE_MSG = "서비스가 점검중입니다.";
}
