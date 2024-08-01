package gift;

import gift.domain.*;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.*;
import gift.service.KakaoService;
import gift.service.OptionService;
import gift.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private KakaoService kakaoService;

    @Mock
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OrderService orderService;

    private Member member;
    private Product product;
    private Option option;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "test@example.com", "password");
        product = new Product(1L, "Test Product", 1000L, "http://example.com/image.png", new Category("Test Category", "Red", "http://example.com/image.png", "Description"));
        option = new Option("Test Option", 10, product);
        option.setId(1L);

        orderRequest = new OrderRequest(option.getId(), 2, "Happy Birthday!");
    }

    @Test
    @DisplayName("주문 성공 테스트")
    void testPlaceOrder_Success() throws Exception {
        when(optionRepository.findById(any(Long.class))).thenReturn(Optional.of(option));
        doNothing().when(optionService).subtractOptionQuantity(any(Long.class), anyString(), anyInt());
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        OrderResponse orderResponse = orderService.placeOrder(orderRequest, member);

        verify(optionRepository, times(1)).findById(any(Long.class));
        verify(optionService, times(1)).subtractOptionQuantity(any(Long.class), anyString(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kakaoService, times(1)).sendOrderMessage(any(Long.class), any(Order.class));

        assertThat(orderResponse.getQuantity(), is(2));
        assertThat(orderResponse.getMessage(), is("Happy Birthday!"));
    }

    @Test
    @DisplayName("주문 실패 테스트 - 옵션 찾기 실패")
    void testPlaceOrder_OptionNotFound() throws Exception {
        when(optionRepository.findById(eq(1L))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderService.placeOrder(orderRequest, member);
        });

        assertThat(exception.getMessage(), is(equalTo("해당하는 옵션이 없습니다.")));
        verify(optionRepository, times(1)).findById(eq(1L));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(kakaoService, times(0)).sendOrderMessage(any(Long.class), any(Order.class));
    }

    @Test
    @DisplayName("주문 실패 테스트 - 옵션 수량 부족")
    void testPlaceOrder_OptionQuantityInsufficient() throws Exception {
        option.setQuantity(1);
        when(optionRepository.findById(any(Long.class))).thenReturn(Optional.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(orderRequest, member);
        });

        assertThat(exception.getMessage(), is(equalTo("옵션 잔여 개수가 요청 개수보다 적습니다.")));
        verify(optionRepository, times(1)).findById(any(Long.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(kakaoService, times(0)).sendOrderMessage(any(Long.class), any(Order.class));
    }

}
