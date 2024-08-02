package gift.client;

import gift.dto.KakaoUserResponse;
import gift.entity.Order;

public interface KakaoApi {
    String getAccessToken(String authorizationCode);

    KakaoUserResponse getUserInfo(String accessToken);

    void sendMessageToMe(String kakaoAccessToken, Order order);
}
