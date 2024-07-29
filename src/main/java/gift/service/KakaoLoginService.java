package gift.service;


import gift.dto.KakaoUserDTO;
import gift.dto.Response.AccessTokenResponse;

public interface KakaoLoginService {
    AccessTokenResponse getAccessToken(String code);
    KakaoUserDTO getUserInfo(String accessToken);
    String extractNickname(KakaoUserDTO userInfo);
    void sendMessage(String accessToken, String nickname);
    void sendLoginMessage(String accessToken, String nickname);
}
