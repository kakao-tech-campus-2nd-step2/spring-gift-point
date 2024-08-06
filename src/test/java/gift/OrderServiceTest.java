package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchReflectiveOperationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.OrderEvent;
import gift.domain.Product;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.ProductResponseDto;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.ProductService;
import gift.service.WishService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductService productService;
    @Mock
    private WishService wishService;
    @Mock
    private KakaoService kakaoService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private OrderService orderService;
    private OrderRequestDto orderRequestDto;
    private Member member;
    private Product product;
    private Option option;

    @BeforeEach
    void setUp() {
        orderRequestDto = new OrderRequestDto(1L, 1L, 3, "message", 0);
        member = new Member(1L, "email@email.com", "password");
        member.setAccessToken("testAccessToken");
        Category category = new Category("category", "color", "image", "");
        product = new Product(1L, "Test Product", 100, "image.jpg", category);
        option = new Option(1L, "Test Option", 10, product);
        product.addOption(option);
    }

    @Test
    void processOrder() {
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
        OrderResponseDto orderResponseDto = new OrderResponseDto(1L, option.getId(), orderRequestDto.quantity(), LocalDateTime.now(), orderRequestDto.message());
        Order savedOrder = new Order(1L, orderRequestDto.productId(),
            orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message(), 20_000);

        given(productService.getProductById(orderRequestDto.productId())).willReturn(productResponseDto);
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        OrderResponseDto result = orderService.processOrder(orderRequestDto, member);

        verify(applicationEventPublisher, times(1)).publishEvent(any(OrderEvent.class));

        assertThat(result.optionId()).isEqualTo(1L);
        assertThat(result.quantity()).isEqualTo(3);
        assertThat(result.message()).isEqualTo("message");

    }

    @Test
    void testCreateOrder() {
        Order savedOrder = new Order(1L, orderRequestDto.productId(),
            orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message(), 20_000);
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        OrderResponseDto orderResponseDto = orderService.createOrder(member.getId(), orderRequestDto, 20_000);

        verify(orderRepository).save(any(Order.class));

        assertThat(orderResponseDto.optionId()).isEqualTo(1L);
        assertThat(orderResponseDto.quantity()).isEqualTo(3);
        assertThat(orderResponseDto.message()).isEqualTo("message");
        System.out.println(orderResponseDto);
    }

    @Test
    void testSendOrderMessage() throws JsonProcessingException {
        Order savedOrder = new Order(1L, orderRequestDto.productId(),
            orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message(), 20_000);
        given(orderRepository.findById(any(Long.class))).willReturn(Optional.of(savedOrder));
        given(productRepository.findById(orderRequestDto.productId())).willReturn(Optional.of(product));

        orderService.sendOrderMessage(1L, member);

        verify(kakaoService).sendKakaoMessageToMe(eq(member.getAccessToken()), any(KakaoMessageDto.class));
    }
}
