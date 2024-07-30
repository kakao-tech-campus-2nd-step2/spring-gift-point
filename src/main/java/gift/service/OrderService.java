package gift.service;

import static gift.controller.order.OrderMapper.toOrder;
import static gift.controller.order.OrderMapper.toOrderResponse;
import static gift.util.KakaoUtil.SendKakaoMessageToMe;

import gift.controller.auth.KakaoTokenResponse;
import gift.controller.order.OrderMapper;
import gift.controller.order.OrderRequest;
import gift.controller.order.OrderResponse;
import gift.domain.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = orderPage.stream()
            .map(OrderMapper::toOrderResponse).toList();
        return new PageImpl<>(orderResponses, pageable, orderPage.getTotalElements());
    }

    @Transactional
    public OrderResponse save(UUID memberId, OrderRequest orderRequest) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            optionService.findById(orderRequest.optionId()).getProductId());
        Order target = orderRepository.save(
            toOrder(optionService.findById(orderRequest.optionId()), orderRequest.quantity(),
                LocalDateTime.now(),
                orderRequest.message()));
        KakaoTokenResponse token = kakaoTokenService.findAccessTokenByMemberId(memberId);
        SendKakaoMessageToMe(orderRequest, token);
        return toOrderResponse(target);
    }
}