package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchReflectiveOperationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.ProductService;
import gift.service.WishService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @InjectMocks
    private OrderService orderService;
    private OrderRequestDto orderRequestDto;
    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        orderRequestDto = new OrderRequestDto(1L, 1L, 3, "message");
        member = new Member("email@email.com", "password");
        member.setAccessToken("testAccessToken");
        Category category = new Category("category", "color", "image", "");
        product = new Product(1L, "Test Product", 100, "image.jpg", category);
        Option option = new Option(1L, "Test Option", 10, product);
        product.addOption(option);
    }

    @Test
    void testProcessOrder() throws JsonProcessingException {
        Order savedOrder = new Order(2L, orderRequestDto.productId(), orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message());
        given(productRepository.findById(orderRequestDto.productId())).willReturn(Optional.of(product));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        OrderResponseDto orderResponseDto = orderService.processOrder(orderRequestDto, member);

        verify(productService).decreaseOptionQuantity(orderRequestDto.productId(), orderRequestDto.optionId(), orderRequestDto.quantity());
        verify(wishService).deleteProductFromWishList(member.getId(), orderRequestDto.productId());
        verify(orderRepository).save(any(Order.class));
        verify(kakaoService).sendKakaoMessageToMe(eq(member.getAccessToken()), any(KakaoMessageDto.class));

        assertThat(orderResponseDto.optionId()).isEqualTo(1L);
        assertThat(orderResponseDto.quantity()).isEqualTo(3);
        assertThat(orderResponseDto.message()).isEqualTo("message");
        System.out.println(orderResponseDto);
    }

    @Test
    void testCreateOrder() {
        Order savedOrder = new Order(2L, orderRequestDto.productId(), orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message());
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto);

        verify(orderRepository).save(any(Order.class));

        assertThat(orderResponseDto.id()).isEqualTo(2L);
        assertThat(orderResponseDto.optionId()).isEqualTo(1L);
        assertThat(orderResponseDto.quantity()).isEqualTo(3);
        assertThat(orderResponseDto.message()).isEqualTo("message");
        System.out.println(orderResponseDto);
    }

    @Test
    void testSendOrderMessage() throws JsonProcessingException {
        given(productRepository.findById(orderRequestDto.productId())).willReturn(Optional.of(product));

        orderService.sendOrderMessage(orderRequestDto, member);

        verify(kakaoService).sendKakaoMessageToMe(eq(member.getAccessToken()), any(KakaoMessageDto.class));
    }
}
