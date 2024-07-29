package gift.client;

import gift.entity.Order;
import gift.dto.KakaoUserResponse;

public interface KakaoApi {
    String getAccessToken(String authorizationCode);
    KakaoUserResponse getUserInfo(String accessToken);
    void sendMessageToMe(String kakaoAccessToken, Order order);
}
