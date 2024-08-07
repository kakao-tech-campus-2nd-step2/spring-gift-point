package gift.constant;

public class ErrorMessage {
    public static final String LENGTH_ERROR_MSG = "상품 이름은 공백 포함 15자를 초과할 수 없습니다.";
    public static final String SPECIAL_CHAR_ERROR_MSG = "특수 문자는 (), [], +, -, &, /, _ 외에는 사용할 수 없습니다.";
    public static final String KAKAO_CONTAIN_ERROR_MSG = "상품 이름에 \"카카오\"가 포함된 경우에는 담당 MD와 협의가 필요합니다.";
    public static final String REQUIRED_FIELD_MSG = "필수로 입력해야 하는 항목입니다.";
    public static final String POSITIVE_NUMBER_REQUIRED_MSG = "0이상의 값을 입력해 주세요.";
    public static final String EMAIL_PATTERN_ERROR_MSG = "이메일 형식이 잘못되었습니다.";
    public static final String DATA_NOT_FOUND_ERROR_MSG = "데이터를 찾을 수 없습니다.";
    public static final String INVALID_QUANTITY_ERROR_MSG = "옵션 수량은 1개 이상 1억 개 미만이어야 합니다.";
    public static final String DUPLICATE_OPTION_NAME_MSG = "하나의 상품에 중복된 이름의 옵션은 생성할 수 없습니다.";
    public static final String INVALID_AMOUNT_ERROR_MSG = "1 이상, 남은 물품 수 미만의 수량만 차감할 수 있습니다.";
    public static final String KAKAO_LOGIN_FAILED_ERROR_MSG = "카카오 로그인에 실패하였습니다.";
    public static final String ALREADY_REGISTERED_ERROR_MSG = "이미 가입된 회원입니다.";
    public static final String SEND_MSG_FAILED_ERROR_MSG = "'나에게 메세지 보내기'에 실패하였습니다.";
    public static final String KAKAO_LOGIN_USER_MSG = "카카오 로그인 이용 회원입니다.";
}
