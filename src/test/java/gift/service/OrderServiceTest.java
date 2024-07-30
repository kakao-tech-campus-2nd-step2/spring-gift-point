/*
package gift.service;

import gift.dto.OrderDTO;
import gift.model.Option;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private OrderService orderService;
    private OptionRepository optionRepository;
    private WishlistRepository wishlistRepository;
    private OrderRepository orderRepository;
    private KakaoService kakaoService;

    private User user;
    private OrderDTO orderDTO;
    private Option option;

    @BeforeEach
    public void setUp() {
        optionRepository = Mockito.mock(OptionRepository.class);
        wishlistRepository = Mockito.mock(WishlistRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        kakaoService = Mockito.mock(KakaoService.class);
        orderService = new OrderService(optionRepository, wishlistRepository, orderRepository, kakaoService, null);

        user = new User("user@example.com", "password");
        orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("Please handle this order with care.");

        option = new Option();
        option.setId(1L);
        option.setName("Test Option");
        option.setQuantity(10);
    }

    @Test
    public void testCreateOrder() {
        when(optionRepository.findById(orderDTO.getOptionId())).thenReturn(Optional.of(option));

        OrderDTO createdOrder = orderService.createOrder(orderDTO, user.getEmail());

        assertEquals(orderDTO.getOptionId(), createdOrder.getOptionId());
        assertEquals(orderDTO.getQuantity(), createdOrder.getQuantity());
        assertEquals(orderDTO.getMessage(), createdOrder.getMessage());
    }
}

 */