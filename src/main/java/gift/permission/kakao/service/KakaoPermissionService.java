package gift.permission.kakao.service;

import static gift.global.utility.MultiValueMapConverter.bodyConvert;
import static gift.global.utility.MultiValueMapConverter.paramConvert;
import static gift.permission.util.PlatformCodeUtil.KAKAO_CODE;

import gift.global.client.ServerClient;
import gift.global.component.KakaoProperties;
import gift.global.dto.TokenDto;
import gift.global.utility.MultiValueMapConverter;
import gift.permission.kakao.dto.KaKaoTokenRequestBodyDto;
import gift.permission.kakao.dto.KakaoAuthRequestDto;
import gift.permission.kakao.dto.KakaoIdResponseDto;
import gift.permission.kakao.dto.KakaoTokenResponseDto;
import gift.permission.user.service.UserService;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class KakaoPermissionService {

    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final ServerClient serverClient;
    private static final String GET_AUTHORIZATION_CODE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String GET_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String GET_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    public KakaoPermissionService(KakaoProperties kakaoProperties,
        UserService userService, ServerClient serverClient) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.serverClient = serverClient;
    }

    // 카카오 로그인 step1에 해당하는 메서드
    public String kakaoAuthorize() {
        var params = paramConvert(KakaoAuthRequestDto.of(kakaoProperties));

        return serverClient.getPage(GET_AUTHORIZATION_CODE_URL, params);
    }

    // 카카오 로그인 step2, 3에 해당하는 메서드
    public TokenDto kakaoLogin(String code) {
        // step2
        var token = getKakaoToken(code);

        // step3
        var platformUniqueId = getPlatformUniqueId(token);
        return userService.login(platformUniqueId, KAKAO_CODE, token.accessToken(),
            token.expiresIn());
    }

    // 카카오 로그인 step2에 해당하는 메서드
    private KakaoTokenResponseDto getKakaoToken(String code) {
        var body = paramConvert(
            KaKaoTokenRequestBodyDto.of(kakaoProperties, code));
        Consumer<HttpHeaders> headersConsumer = headers -> {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        };

        var response = serverClient.postRequest(GET_TOKEN_URI, body, headersConsumer,
            KakaoTokenResponseDto.class);

        return response.getBody();
    }

    // token을 받아온 후, token을 사용하여 식별자로 사용할 id를 받아오는 메서드 (step3에서 사용)
    private long getPlatformUniqueId(KakaoTokenResponseDto kakaoTokenResponseDto) {
        var accessToken = kakaoTokenResponseDto.accessToken();

        Consumer<HttpHeaders> headersConsumer = headers -> {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);
        };

        var response = serverClient.getRequest(GET_USER_INFO_URI, headersConsumer,
            KakaoIdResponseDto.class);

        return response.getBody().id();
    }
}
