package gift.service;


import gift.dto.KakaoUserDTO;
import gift.dto.Response.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(KakaoUserDTO kakaoUserDTO, Long wishlistId, String accessToken);
}
