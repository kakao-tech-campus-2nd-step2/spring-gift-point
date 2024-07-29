package gift.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import gift.domain.AppUser;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.Role;
import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.repository.OrderRepository;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OptionService optionService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private WishListService wishListService;

    @InjectMocks
    private OrderService orderService;

    private Option option;
    private Order expectedOrder;
    private AppUser user;

    @BeforeEach
    public void setUp() {
        Category defaultCategory = new Category("기본", "기본 카테고리");
        Product product = new Product("test", 100, "image", user, defaultCategory);
        product.setId(5L);
        user = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        option = new Option("option", 10, 300, product);
        expectedOrder = new Order(option, user, 5, "message");
    }

    @Test
    public void testCreateOrder() {
        OrderRequest orderRequest = new OrderRequest(1L, 5, "message");
        when(optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity())).thenReturn(option);
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
        doNothing().when(wishListService).deleteWishIfExists(user.getId(), option.getProduct().getId());

        OrderResponse result = orderService.createOrder(user, orderRequest);

        assertThat(result.getQuantity()).isEqualTo(expectedOrder.getQuantity());
        assertThat(result.getMessage()).isEqualTo(expectedOrder.getMessage());
    }
}
