package gift.service;

import gift.common.enums.SocialLoginType;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.event.OrderEventDto;
import gift.model.Member;
import gift.model.Option;
import gift.model.Orders;
import gift.model.Product;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private final KakaoTokenService kakaoTokenService;
    private final ProductService productService;
    private final KakaoApiCaller kakaoApiCaller;

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, MemberRepository memberRepository, KakaoTokenService kakaoTokenService, ProductService productService, KakaoApiCaller kakaoApiCaller, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.kakaoTokenService = kakaoTokenService;
        this.productService = productService;
        this.kakaoApiCaller = kakaoApiCaller;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse createOrder(Long memberId, OrderRequest orderRequest) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(orderRequest.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product with productId " + orderRequest.productId() + " not found"));
        Option option = product.findOptionByOptionId(orderRequest.optionId());
        Orders orders = orderRepository.save(new Orders(product.getId(), option.getId(), memberId,
                product.getName(), option.getName(), product.getPrice(), orderRequest.quantity(), orderRequest.message()));
        productService.subtractQuantity(orders.getProductId(), orders.getOptionId(), orders.getQuantity());

        eventPublisher.publishEvent(new OrderEventDto(product.getId(), memberId, orders.getId()));
        return OrderResponse.from(orders);
    }

    public void sendKakaoMessage(Long memberId, Long orderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member Not Exists."));
        if(member.getLoginType() != SocialLoginType.KAKAO) {
            return;
        }

        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Exists."));
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.sendKakaoMessage(accessToken, orders);
    }

}
