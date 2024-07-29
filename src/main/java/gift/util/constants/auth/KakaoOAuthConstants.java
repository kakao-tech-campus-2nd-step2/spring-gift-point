package gift.util.constants.auth;

public class KakaoOAuthConstants {

    public static final String TOKEN_RESPONSE_ERROR = "토큰 응답이 올바르지 않습니다.";
    public static final String TOKEN_FAILURE_ERROR = "토큰 발급 요청이 실패했습니다: ";
    public static final String UNLINK_RESPONSE_ERROR = "연결 끊기 응답이 올바르지 않습니다.";
    public static final String UNLINK_FAILURE_ERROR = "연결 끊기 요청이 실패했습니다: ";
    public static final String SCOPES_FAILURE_ERROR = "동의 내역 가져오기 요청이 실패했습니다: ";
    public static final String USERINFO_RESPONSE_ERROR = "사용자 정보 응답이 올바르지 않습니다.";
    public static final String USERINFO_FAILURE_ERROR = "사용자 정보 가져오기 요청이 실패했습니다: ";
    public static final String TOKEN_NOT_FOUND = "카카오 토큰을 찾을 수 없습니다.";

    // URL
    public static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    public static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    public static final String KAKAO_SCOPES_URL = "https://kapi.kakao.com/v2/user/scopes";
}
