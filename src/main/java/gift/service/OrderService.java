package gift.service;

import static gift.controller.order.OrderMapper.toOrder;
import static gift.controller.order.OrderMapper.toOrderResponse;
import static gift.util.KakaoUtil.SendKakaoMessageToMe;

import gift.controller.auth.KakaoTokenResponse;
import gift.controller.order.OrderRequest;
import gift.controller.order.OrderResponse;
import gift.domain.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishService wishService;
    private final OptionService optionService;
    private final KakaoTokenService kakaoTokenService;

    public OrderService(OrderRepository orderRepository, WishService wishService,
        OptionService optionService, KakaoTokenService kakaoTokenService) {
        this.orderRepository = orderRepository;
        this.wishService = wishService;
        this.optionService = optionService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @Transactional
    public OrderResponse save(UUID memberId, OrderRequest orderRequest) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            optionService.getOptionResponseById(orderRequest.optionId()).productId());
        Order target = orderRepository.save(
            toOrder(optionService.findById(orderRequest.optionId()), LocalDateTime.now(),
                orderRequest.message()));
        KakaoTokenResponse token = kakaoTokenService.findAccessTokenByMemberId(memberId);
        SendKakaoMessageToMe(orderRequest, token);
        return toOrderResponse(target.getId(), target.getOption().getId(), orderRequest.quantity(),
            target.getOrderDateTime(), target.getMessage());
    }
}