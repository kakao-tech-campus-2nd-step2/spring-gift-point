package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderDTO;
import gift.entity.OrderEntity;
import gift.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    private OptionService optionService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishListService wishListService;

    @Mock
    private KakaoUserService kakaoUserService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_Success() throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("Test Message");

        Long userId = 1L;
        String email = "test@example.com";
        String accessToken = "testAccessToken";

        orderService.createOrder(orderDTO, userId, email, accessToken);

        verify(optionService, times(1)).subtractOptionQuantity(orderDTO.getOptionId(), orderDTO.getQuantity());
        verify(wishListService, times(1)).removeOptionFromWishList(userId, orderDTO.getOptionId());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(kakaoUserService, times(1)).sendOrderMessage(eq(accessToken), eq(orderDTO));
    }

    @Test
    void createOrder_error() throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("Test Message");

        Long userId = 1L;
        String email = "test@example.com";
        String accessToken = "testAccessToken";

        doThrow(new JsonProcessingException("카카오 메시지 전송 실패") {}).when(kakaoUserService).sendOrderMessage(accessToken, orderDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDTO, userId, email, accessToken);
        });

        assertEquals("카카오 메시지 전송 실패: 카카오 메시지 전송 실패", exception.getMessage());

        verify(optionService, times(1)).subtractOptionQuantity(orderDTO.getOptionId(), orderDTO.getQuantity());
        verify(wishListService, times(1)).removeOptionFromWishList(userId, orderDTO.getOptionId());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(kakaoUserService, times(1)).sendOrderMessage(eq(accessToken), eq(orderDTO));
    }
}