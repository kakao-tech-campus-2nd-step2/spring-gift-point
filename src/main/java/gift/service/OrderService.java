package gift.service;

import gift.common.enums.SocialLoginType;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.controller.dto.response.PagingResponse;
import gift.event.OrderEventDto;
import gift.model.Member;
import gift.model.Option;
import gift.model.Orders;
import gift.model.Product;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.service.dto.OrderDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void createOrder(Long memberId, OrderRequest orderRequest) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(orderRequest.productId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
        Option option = product.findOptionByOptionId(orderRequest.optionId());
        System.out.println("00000000000000000000000000");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
        System.out.println("1111111111111111111111111111");
        member.usePoint(orderRequest.point());
        System.out.println("222222222222222222222222222");
        int price = product.usePoint(orderRequest.point());
        System.out.println("333333333333333333333333333333");

        Orders orders = orderRepository.save(new Orders(product.getId(), option.getId(), memberId, product.getName(),
                product.getImageUrl(), option.getName(), price, orderRequest.quantity(), orderRequest.message(), orderRequest.point()));
        productService.subtractQuantity(orders.getProductId(), orders.getOptionId(), orders.getQuantity());

        eventPublisher.publishEvent(OrderEventDto.toDto(orders));
    }

    public void sendKakaoMessage(OrderDto orderDto) {
        Member member = memberRepository.findById(orderDto.memberId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
        if(member.getLoginType() != SocialLoginType.KAKAO) {
            return;
        }
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(orderDto.memberId());
        kakaoApiCaller.sendKakaoMessage(accessToken, orderDto);
    }

    public PagingResponse<OrderResponse> findOrdersByMemberId(Long memberId, Pageable pageable) {
        Page<OrderResponse> pages = orderRepository.findByMemberId(memberId, pageable)
                .map(OrderResponse::from);
        return PagingResponse.from(pages);
    }
}
