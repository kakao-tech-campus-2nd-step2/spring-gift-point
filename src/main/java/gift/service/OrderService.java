package gift.service;

import gift.DTO.kakao.KakaoMemberInfo;
import gift.DTO.order.OrderRequest;
import gift.DTO.order.OrderResponse;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final MemberService memberService;
    private final KakaoService kakaoService;

    public OrderService(
        OrderRepository orderRepository,
        OptionService optionService,
        MemberService memberService,
        KakaoService kakaoService
    ) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::fromEntity)
            .toList();
    }

    @Transactional
    public OrderResponse makeOrder(String accessToken, OrderRequest orderRequest) {
        KakaoMemberInfo kakaoMemberInfo = kakaoService.getMemberInfo(accessToken);
        Member member = memberService.getMemberByKakaoId(kakaoMemberInfo.id());
        Long optionId = orderRequest.optionId();
        Long quantity = orderRequest.quantity();

        Option option = optionService.getOptionById(optionId);
        optionService.decrementOptionQuantity(optionId, quantity);
        Order newOrder = new Order(quantity,
                                    orderRequest.message(),
                                    LocalDateTime.now(),
                                    option,
                                    member);
        Order savedOrder = orderRepository.save(newOrder);

        // 상품 id, 상품명, 옵션  id, 옵션 이름, 옵션 수량, 구매자, 주문메시지

        kakaoService.sendKakaoMessage(accessToken, newOrder);
        return OrderResponse.fromEntity(savedOrder);
    }
}
