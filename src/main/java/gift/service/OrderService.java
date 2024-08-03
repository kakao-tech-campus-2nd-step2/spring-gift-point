package gift.service;

import gift.dto.OrderDTO;
import gift.dto.KakaoUserDTO;

public interface OrderService {
    OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken);
    OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken, int pointsToUse);
}
