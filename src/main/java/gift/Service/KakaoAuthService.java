package gift.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.DTO.KakaoUserDTO;

import java.util.Map;

@Service
public class KakaoAuthService {

    @Autowired
    private KakaoUserService kakaoUserService;

    @Autowired
    private KakaoOAuthService kakaoOAuthService;

    public KakaoUserDTO handleKakaoLogin(String code) {
        // 카카오 인증 서버와 통신하여 사용자 정보를 가져오는 로직(인가 코드->토큰->정보)
        Map<String, String> tokens = kakaoOAuthService.getTokens(code);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");
        KakaoUserDTO kakaoUserDTO = kakaoOAuthService.getUserInfo(accessToken);

        // 사용자 정보를 저장하거나 업데이트
        return kakaoUserService.saveOrUpdateUser(kakaoUserDTO, accessToken, refreshToken);
    }
}
