package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.OrderEvent;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderPagedResponseDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.ProductResponseDto;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import java.util.NoSuchElementException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final KakaoService kakaoService;
    private final ProductService productService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, KakaoService kakaoService,
        ProductService productService, ApplicationEventPublisher applicationEventPublisher) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
        this.productService = productService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderResponseDto processOrder(OrderRequestDto orderRequestDto, Member member) {
        ProductResponseDto product = productService.getProductById(orderRequestDto.productId());

        // 재고 처리
        productService.decreaseOptionQuantity(orderRequestDto.productId(), orderRequestDto.optionId(),orderRequestDto.quantity());
        // 포인트 차감
        member.subtractPoints(orderRequestDto.point()); // 포인트 입력값 예외 처리까지 수행
        Integer finalPrice = product.price() * orderRequestDto.quantity() - orderRequestDto.point();
        // 주문 생성 및 저장
        OrderResponseDto orderResponseDto = createOrder(member.getId(), orderRequestDto, finalPrice);
        // 포인트 적립
        int amount = (int) Math.round(product.price() * orderResponseDto.quantity() * 0.01);
        member.addPoints(amount);

        // 위시리스트 삭제 & 주문 메시지 발송
        OrderEvent orderEvent = new OrderEvent(orderResponseDto.id(), member, product.id());
        applicationEventPublisher.publishEvent(orderEvent);

        return orderResponseDto;
    }

    @Transactional
    public OrderResponseDto createOrder(Long memberId, OrderRequestDto orderRequestDto, Integer finalPrice) {
        Order order = new Order(
            memberId,
            orderRequestDto.productId(),
            orderRequestDto.optionId(),
            orderRequestDto.quantity(),
            orderRequestDto.message(),
            finalPrice
        );
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.convertToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public void sendOrderMessage(Long orderId, Member member) {
        try {
            kakaoService.sendKakaoMessageToMe(
                member.getAccessToken(),
                new KakaoMessageDto(
                    "text",
                    createTextDetail(orderId, member),
                    "https://gift.kakao.com",
                    "https://gift.kakao.com"
                )
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public String createTextDetail(Long orderId, Member member) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("[Text] 해당 id의 주문 없음: " + orderId));
        Product product = productRepository.findById(order.getProductId())
            .orElseThrow(() -> new NoSuchElementException("[Text] 해당 id의 상품 없음: " + order.getProductId()));
        Option option = product.getOptionById(order.getOptionId());

        String text = "계정 이메일: " + member.getEmail()
            + "\n주문한 상품명: " + product.getName()
            + "\n주문한 옵션명: " + option.getName()
            + "\n주문 수량: " + order.getQuantity()
            + "\n요청 사항: " + order.getMessage()
            + "\n최종 결제 금액: " + order.getFinalPrice()
            + "\n남은 포인트: " + member.getPoints();

        return text;
    }

    @Transactional(readOnly = true)
    public Page<OrderPagedResponseDto> findByMemberId(Long id, Pageable pageable) {

        Page<Order> orderPage= orderRepository.findByMemberId(id, pageable);
        return orderPage.map(order -> {
            ProductResponseDto product = productService.getProductById(order.getProductId());

            return OrderPagedResponseDto.convertToDto(
                order,
                product.name(),
                product.price(),
                product.imageUrl()
            );
        });
    }
}
