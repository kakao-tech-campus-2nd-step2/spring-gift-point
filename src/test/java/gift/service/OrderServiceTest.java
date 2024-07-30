package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("성공적인 주문 생성 테스트")
    void testCreateOrder_Success() {
        // Given
        OrderRequest request = new OrderRequest(1L, 5, "Thank you!");
        Option option = new Option("Option 1", null, 10);
        ReflectionTestUtils.setField(option, "id", 1L); // Option ID 설정
        Order order = new Order(option, 5, LocalDateTime.now(), "Thank you!");

        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(tokenService.isJwtToken(anyString())).thenReturn(true);
        when(tokenService.extractEmailFromToken(anyString())).thenReturn("test@example.com");

        // When
        OrderResponse response = orderService.createOrder(request, "jwtToken");

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getOptionId());
        assertEquals(5, response.getQuantity());
        assertEquals("Thank you!", response.getMessage());

        verify(optionRepository, times(1)).findById(1L);
        verify(optionRepository, times(1)).save(any(Option.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(wishRepository, times(1)).deleteByOptionId(1L);
        verify(kakaoMessageService, never()).sendMessage(any(OrderResponse.class), anyString());
    }

    @Test
    @DisplayName("잘못된 옵션 ID로 인한 주문 생성 실패 테스트")
    void testCreateOrder_InvalidOptionId() {
        // Given
        OrderRequest request = new OrderRequest(1L, 5, "Thank you!");

        when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request, "jwtToken");
        });

        verify(optionRepository, times(1)).findById(1L);
        verify(optionRepository, never()).save(any(Option.class));
        verify(orderRepository, never()).save(any(Order.class));
        verify(wishRepository, never()).deleteByOptionId(anyLong());
        verify(kakaoMessageService, never()).sendMessage(any(OrderResponse.class), anyString());
    }

    @Test
    @DisplayName("카카오 토큰으로 주문 생성 테스트")
    void testCreateOrder_KakaoToken() {
        // Given
        OrderRequest request = new OrderRequest(1L, 5, "Thank you!");
        Option option = new Option("Option 1", null, 10);
        ReflectionTestUtils.setField(option, "id", 1L); // Option ID 설정
        Order order = new Order(option, 5, LocalDateTime.now(), "Thank you!");

        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(tokenService.isJwtToken(anyString())).thenReturn(false);

        // When
        OrderResponse response = orderService.createOrder(request, "kakaoToken");

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getOptionId());
        assertEquals(5, response.getQuantity());
        assertEquals("Thank you!", response.getMessage());

        verify(optionRepository, times(1)).findById(1L);
        verify(optionRepository, times(1)).save(any(Option.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(wishRepository, times(1)).deleteByOptionId(1L);
        verify(kakaoMessageService, times(1)).sendMessage(any(OrderResponse.class), eq("kakaoToken"));
    }
}