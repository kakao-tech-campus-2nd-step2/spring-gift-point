package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.OrderEvent;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import java.util.NoSuchElementException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final KakaoService kakaoService;
    private final ProductService productService;
    private final WishService wishService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, KakaoService kakaoService,
        ProductService productService, WishService wishService,
        ApplicationEventPublisher applicationEventPublisher) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
        this.productService = productService;
        this.wishService = wishService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderResponseDto processOrder(OrderRequestDto orderRequestDto, Member member) {
        // 재고 처리
        productService.decreaseOptionQuantity(orderRequestDto.productId(), orderRequestDto.optionId(),orderRequestDto.quantity());
        // 주문 생성 및 저장
        OrderResponseDto orderResponseDto = createOrder(orderRequestDto);

        // 위시리스트 삭제 & 주문 메시지 발송
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, member);
        applicationEventPublisher.publishEvent(orderEvent);

        return orderResponseDto;
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.productId(), orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message());
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.convertToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public void sendOrderMessage(OrderRequestDto orderRequestDto, Member member) {
        try {
            kakaoService.sendKakaoMessageToMe(
                member.getAccessToken(),
                new KakaoMessageDto(
                    "text",
                    createTextDetail(orderRequestDto, member.getEmail()),
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
    public String createTextDetail(OrderRequestDto orderRequestDto, String email) {
        Product product = productRepository.findById(orderRequestDto.productId())
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + orderRequestDto.productId()));
        Option option = product.getOptionById(orderRequestDto.optionId());

        String text = "계정 이메일: " + email
            + "\n주문한 상품명: " + product.getName()
            + "\n주문한 옵션명: " + option.getName()
            + "\n주문 수량: " + orderRequestDto.quantity()
            + "\n요청 사항: " + orderRequestDto.message();

        return text;
    }
}
