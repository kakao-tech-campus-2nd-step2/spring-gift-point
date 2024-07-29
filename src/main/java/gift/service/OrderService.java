package gift.service;

import gift.dto.OrderDTO;
import gift.dto.KakaoUserDTO;

public interface OrderService {
    OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long optionId, String accessToken);
}
