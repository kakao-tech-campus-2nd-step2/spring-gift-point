package gift.exception.exceptionMessage;

public class ExceptionMessage {

    public static final String CATEGORY_NAME_DUPLICATION = "중복된 카테고리 이름 입니다.";
    public static final String EMAIL_DUPLICATION = "중복된 이메일 이름 입니다.";
    public static final String ALREADY_TOKEN_GET_EMAIL = "이미 토큰을 발급한 아이디 입니다.";
    public static final String CATEGORY_NOT_FOUND = "해당 카테고리는 존재하지 않습니다.";
    public static final String PRODUCT_NOT_FOUND = "해당 상품은 존재하지 않습니다.";
    public static final String WISH_NOT_FOUND = "해당 WISH는 존재하지 않습니다.";
    public static final String FORBIDDEN_MESSAGE =  "접근할 수 있는 권한을 가지고 있지 않습니다.";
    public static final String NAME_IN_KAKAO =  "상품이름에 카카오가 들어가 있습니다. 담당 MD와 협의하세요.";
    public static final String MEMBER_NOT_FOUND =  "아이디 또는 비밀번호가 일치하지 않습니다.";
    public static final String NOT_EXISTS_MEMBER =  "존재하지 않는 회원입니다,";
    public static final String UNAUTHORIZATION_EXCEPTION =  "인증되지 않은 사용자 입니다. 다시 로그인 해주세요.";
    public static final String OPTION_NAME_DUPLICATION =  "중복된 옵션 이름 입니다.";
    public static final String OPTION_NOT_FOUND =  "해당 옵션은 존재하지 않습니다.";
    public static final String DENY_OPTION_DELETE =  "상품의 옵션은 항상 1개 이상이어야 합니다.";
    public static final String OPTION_QUANTITY_NOT_MINUS =  "옵션 수량은 0보다 커야합니다.";
    public static final String JSON_PROCESSING_ERROR =  "처리할 수 없는 JSON 입니다.";
    public static final String API_BAD_REQUEST =  "API 사용에 필요한 파라미터가 잘못 되었습니다.";
    public static final String API_UNAUTHORIZED =  "API 사용 인증 자격 증명에 실패했습니다.";
    public static final String API_FORBIDDEN =  "API 사용 권한이 없습니다.";
    public static final String API_TOO_MANY_REQUESTS =  "API 사용에 정해진 사용량 또는 요청 한도를 초과했습니다.";
    public static final String API_INTERNAL_SERVER_ERROR =  "API 서버 오류";
    public static final String API_BAD_GATEWAY =  "API 게이트 웨이 오류";
    public static final String API_SERVICE_UNAVAILABLE =  "API 서비스 점검 중";
    public static final String POINT_EXCEED_PURCHASE_AMOUNT =  "포인트는 구매 금액을 넘을 수 없습니다.";
    public static final String POINT_EXCEED_USER_POINT =  "사용 포인트는 보유 포인트양을 넘을 수 없습니다.";
}
