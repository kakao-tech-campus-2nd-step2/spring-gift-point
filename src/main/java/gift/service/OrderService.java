package gift.service;

import gift.dto.order.OrderRequestDTO;
import gift.dto.order.OrderResponseDTO;
import gift.dto.user.KakaoUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final PointService pointService;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoApiService kakaoApiService;

    public OrderService(PointService pointService, OptionService optionService, WishService wishService, KakaoApiService kakaoApiService) {
        this.pointService = pointService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
    }

    @Transactional
    public OrderResponseDTO processOrder(KakaoUserDTO kakaoUserDTO, OrderRequestDTO orderRequestDTO) {
        optionService.subtractOptionQuantity(orderRequestDTO.optionId(), orderRequestDTO.quantity());
        pointService.subtractPoint(kakaoUserDTO.user().getId(), orderRequestDTO.optionId(), orderRequestDTO.quantity());
        wishService.deleteWishOption(kakaoUserDTO.user().getId(), orderRequestDTO.optionId());
        kakaoApiService.sendMessage(kakaoUserDTO.accessToken(), orderRequestDTO);

        return kakaoApiService.getOrderResponseDTO(orderRequestDTO);
    }
}
