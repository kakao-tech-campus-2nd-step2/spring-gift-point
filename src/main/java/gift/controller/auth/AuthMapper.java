package gift.controller.auth;

import gift.config.KakaoConfig;
import gift.controller.member.MemberResponse;
import gift.domain.KakaoToken;
import org.springframework.util.LinkedMultiValueMap;

public class AuthMapper {

    public static LoginResponse toLoginResponse(MemberResponse member) {
        return new LoginResponse(member.id(), member.email(), member.nickName(), member.grade());
    }

    public static KakaoTokenResponse toKakaoTokenResponse(KakaoToken kakaoToken) {
        return new KakaoTokenResponse(kakaoToken.getAccessToken());
    }

    public static LinkedMultiValueMap<String, String> toTokenRequestBody(String grantType, String clientId, String redirectUrl, String code) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUrl);
        requestBody.add("code", code);
        return requestBody;
    }
}