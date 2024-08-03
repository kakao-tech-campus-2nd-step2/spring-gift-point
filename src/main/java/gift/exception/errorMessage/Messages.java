package gift.exception.errorMessage;

public class Messages {
    public static final String NOT_FOUND_PRODUCT_BY_NAME = "해당 이름의 상품이 존재하지 않습니다.";
    public static final String NOT_FOUND_WISH = "위시가 존재하지 않습니다.";
    public static final String NOT_FOUND_PRODUCT_BY_ID= "해당 ID의 상품이 존재하지 않습니다.";
    public static final String NOT_FOUND_MEMBER = "해당 정보를 가진 회원이 존재하지 않습니다.";
    public static final String NOT_FOUND_CATEGORY = "해당 정보를 가진 카테고리가 존재하지 않습니다.";
    public static final String NOT_FOUND_OPTION = "해당 정보를 가진 옵션이 존재하지 않습니다.";
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "이미 존재하는 카테고리 이름입니다.";
    public static final String OPTION_NAME_ALREADY_EXISTS = "이미 존재하는 옵션 이름입니다.";
    public static final String CANNOT_DELETE_LAST_OPTION = "상품의 옵션은 1개 이상이어야 합니다. 옵션을 삭제할 수 없습니다";
    public static final String PRODUCT_OPTION_REQUIRED = "상품의 옵션은 1개 이상이어야 합니다.";
    public static final String INSUFFICIENT_QUANTITY = "옵션의 수량이 부족합니다.";
    public static final String MEMBER_EMAIL_ALREADY_EXISTS = "이미 사용된 이메일 입니다.";
    public static final String PRODUCT_ALREADY_IN_WISHLIST = "이미 위시리스트에 존재하는 상품입니다.";
    public static final String MISSING_AUTHORIZATION_CODE = "인가 코드가 존재하지 않습니다.";
    public static final String RESPONSE_BODY_NULL = "API의 응답값이 null 입니다.";
    public static final String API_BAD_REQUEST =  "API 사용에 필요한 파라미터가 잘못 되었습니다.";
    public static final String API_UNAUTHORIZED =  "API 사용 인증 자격 증명에 실패했습니다.";
    public static final String API_FORBIDDEN =  "API 사용 권한이 없습니다.";
    public static final String API_TOO_MANY_REQUESTS =  "API 사용에 정해진 사용량 또는 요청 한도를 초과했습니다.";
    public static final String API_INTERNAL_SERVER_ERROR =  "API 서버 오류";
    public static final String API_BAD_GATEWAY =  "API 게이트 웨이 오류";
    public static final String API_SERVICE_UNAVAILABLE =  "API 서비스 점검 중";
    public static final String POINTS_CANNOT_BE_NEGATIVE = "포인트는 음수일 수 없습니다.";
    public static final String INSUFFICIENT_POINTS = "포인트가 부족합니다.";
    public static final String POINTS_USAGE_LIMIT = "상품의 50% 이상 포인트를 사용할 수 없습니다.";
}
