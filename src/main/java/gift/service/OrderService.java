package gift.service;

import static gift.controller.order.OrderMapper.toOrder;
import static gift.controller.order.OrderMapper.toOrderResponse;
import static gift.util.KakaoUtil.SendKakaoMessageToMe;

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

    private static final Long DISCOUNT_CONDITION_PRICE = 50000L;
    private static final Double DISCOUNT_RATE = 0.9;

    private final OrderRepository orderRepository;
    private final WishService wishService;
    private final OptionService optionService;
    private final KakaoTokenService kakaoTokenService;
    private final ProductService productService;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, WishService wishService,
        OptionService optionService, KakaoTokenService kakaoTokenService, ProductService productService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.wishService = wishService;
        this.optionService = optionService;
        this.kakaoTokenService = kakaoTokenService;
        this.productService = productService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = orderPage.stream()
            .map(OrderMapper::toOrderResponse).toList();
        return new PageImpl<>(orderResponses, pageable, orderPage.getTotalElements());
    }

    @Transactional
    public OrderResponse save(UUID senderId, UUID recipientId, OrderRequest orderRequest) {
        var option = optionService.findById(orderRequest.optionId());
        long totalPrice = productService.find(option.getProductId()).getPrice() * orderRequest.quantity();
        if (totalPrice >= DISCOUNT_CONDITION_PRICE) totalPrice = (long) (totalPrice * DISCOUNT_RATE);   // 할인 포인트 적용
        memberService.usePoint(senderId, totalPrice);   // 포인트 사용
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());   // 옵션 수량 차감
        wishService.delete(recipientId, option.getProductId());  // 수령자 위시리스트 삭제
        Order order = orderRepository.save(toOrder(option, orderRequest.quantity(), LocalDateTime.now(), orderRequest.message()));  // 주문 내역 저장
        SendKakaoMessageToMe(orderRequest, kakaoTokenService.findAccessTokenByMemberId(recipientId));  // 카카오 메시지 보내기
        return toOrderResponse(order);
    }
}