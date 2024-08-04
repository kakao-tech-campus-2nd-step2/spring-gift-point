package gift.service;

import gift.exception.option.NotFoundOptionsException;
import gift.model.Member;
import gift.model.Options;
import gift.model.Order;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.request.OrderRequest;
import gift.response.order.CreatedOrderResponse;
import gift.response.order.OrderResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final OptionsService optionsService;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository, OptionsService optionsService,
        WishRepository wishRepository, MemberRepository memberRepository,
        KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionsService = optionsService;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    public List<OrderResponse> getOrder(Long memberId) {
        return orderRepository.findByMemberId(memberId)
            .stream()
            .map(OrderResponse::createOrderResponse)
            .toList();
    }

    public CreatedOrderResponse makeOrder(Long memberId, String accessToken,
        OrderRequest orderRequest) {
        CreatedOrderResponse response = addOrder(memberId, orderRequest);
        sendKakaoMessageToMe(accessToken, orderRequest.message());
        return response;
    }

    @Transactional
    public CreatedOrderResponse addOrder(Long memberId, OrderRequest orderRequest) {
        Options option = optionsService.getOption(orderRequest.optionId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(NotFoundOptionsException::new);

        optionsService.subtractQuantity(option.getId(), orderRequest.quantity(),
            orderRequest.productId());
        wishRepository.findByMemberIdAndProductId(memberId, orderRequest.productId())
            .ifPresent(wishRepository::delete);
        member.subPoint(orderRequest.point());
        Order savedOrder = orderRepository.save(new Order(memberId, option,
            orderRequest.quantity(), orderRequest.message()));
        member.addPoint(option.getProduct().getPrice() * orderRequest.quantity());

        return CreatedOrderResponse.createOrderResponse(savedOrder, orderRequest.point());
    }

    public void sendKakaoMessageToMe(String accessToken, String message) {
        kakaoMessageService.sendMessageToMe(accessToken, message);
    }

}
