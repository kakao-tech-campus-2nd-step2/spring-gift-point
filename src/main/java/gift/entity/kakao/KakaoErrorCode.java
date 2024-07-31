package gift.entity.kakao;

import org.springframework.http.HttpStatus;

public enum KakaoErrorCode {
    KAKAO_ALLOWED_REQUEST_ERROR("허용된 요청 횟수를 초과하셨습니다.", HttpStatus.BAD_REQUEST),
    KAKAO_TOKEN_ERROR("카카오 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    KAKAO_SEND_PERMISSION_ERROR("카카오톡 보내기 권한이 없습니다.", HttpStatus.FORBIDDEN),
    KAKAO_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;

    KakaoErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static KakaoErrorCode determineKakaoErrorCode(String statusCode) {
        switch (statusCode) {
            case "400":
                return KAKAO_ALLOWED_REQUEST_ERROR; // Assuming this corresponds to 400
            case "401":
                return KAKAO_TOKEN_ERROR;
            case "403":
                return KAKAO_SEND_PERMISSION_ERROR;
            default:
                return KAKAO_SERVER_ERROR;
        }
    }
}
