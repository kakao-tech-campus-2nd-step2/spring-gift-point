package gift.service;


import gift.dto.KakaoUserDTO;
import gift.dto.Response.OrderResponseDto;

public interface OrderService {
    OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken);
    OrderDTO placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken, int pointsToUse);
}
