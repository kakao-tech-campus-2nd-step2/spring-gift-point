package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    /* Product 관련 예외 */
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다." ),
    PRODUCT_ALREADY_EXIST(HttpStatus.CONFLICT, "상품이 이미 존재합니다." ),

    /* Category 관련 예외 */
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다." ),
    INVALID_COLOR(HttpStatus.BAD_REQUEST, "유효하지 않은 색상 코드입니다. \n예시: #FF0000" ),

    /* Option 관련 예외 */
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션을 찾을 수 없습니다." ),
    OPTION_NAME_DUPLICATE(HttpStatus.CONFLICT, "중복된 이름의 옵션이 존재합니다." ),
    INVALID_OPTION_QUANTITY(HttpStatus.BAD_REQUEST, "옵션 수량은 1개 이상 1억 개 미만입니다." ),
    INVALID_OPTION_NAME(HttpStatus.BAD_REQUEST,
        "옵션 이름은 최대 50자까지 가능하고, 특수 문자는 `( ), [ ], +, -, &, /, _`만 혀용합니다." ),
    LAST_OPTION(HttpStatus.BAD_REQUEST,
        "마지막 남은 옵션은 삭제할 수 없습니다. 다른 옵션을 추가한 후 삭제를 시도해 주세요." ),
    INVALID_SUBTRACTION_QUANTITY(HttpStatus.BAD_REQUEST, "차감할 수량은 1 이상 현재 재고 수량 이하이어야 합니다." ),

    /* User 관련 예외 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다." ),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "사용자가 이미 존재합니다." ),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다." ),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다." ),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다." ),

    /* Wish 관련 예외 */
    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "위시리스트를 찾을 수 없습니다." ),
    INVALID_WISH_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 0보다 큰 수이어야 합니다." ),

    /* Kakao API 관련 예외 */
    KAKAO_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "로그인 할 수 없습니다" ),
    KAKAO_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다." );

    private final HttpStatusCode status;
    private final String message;

    ErrorCode(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
