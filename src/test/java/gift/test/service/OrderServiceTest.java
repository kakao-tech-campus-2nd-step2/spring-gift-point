package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.validation.BindingResult;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.dto.WishlistRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import gift.service.UserService;
import gift.service.WishlistService;

public class OrderServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private UserService userService;

    @Mock
    private RetryTemplate retryTemplate;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OrderService orderService;

    private Option option;
    private Order order;
    private User user;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        product.setId(1L);
        
        option = new Option("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969, product);
        option.setId(1L);

        order = new Order(option, 2, "Please handle this order with care.");
        order.setOrderDateTime(LocalDateTime.now());

        user = new User("test@test.com", "pw");
        user.setId(1L);
    }

    @Test
    void testCreateOrder() throws Throwable {
        String token = "Bearer validToken";
        
        LocalDateTime requestTime = LocalDateTime.now();
        OrderRequest request = new OrderRequest(1L, 2, "Please handle this order with care.");

        when(retryTemplate.execute(any(RetryCallback.class))).thenAnswer(invocation -> {
            RetryCallback<OrderResponse, Exception> callback = invocation.getArgument(0);
            return callback.doWithRetry(null);
        });
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(userService.getUserFromToken(anyString())).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.createOrder(token, request, bindingResult);

        verify(optionRepository).save(any(Option.class));
        verify(orderRepository).save(any(Order.class));
        verify(kakaoMessageService).sendMessage(anyString(), anyString());
        verify(wishlistRepository).findByUserIdAndProductId(anyLong(), anyLong());
        verify(wishlistService, never()).removeWishlist(anyString(), any(WishlistRequest.class), any(BindingResult.class));
        verify(bindingResult).hasErrors();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getOptionId()).isEqualTo(option.getId());
        assertThat(response.getQuantity()).isEqualTo(order.getQuantity());
        assertThat(response.getMessage()).isEqualTo(order.getMessage());
        assertThat(response.getOrderDateTime()).isCloseTo(requestTime, within(1, ChronoUnit.SECONDS));
    }
}
