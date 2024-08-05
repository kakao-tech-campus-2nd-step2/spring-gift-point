package gift.order;

import gift.core.domain.order.Order;
import gift.core.domain.order.OrderRepository;
import gift.core.domain.order.OrderService;
import gift.core.domain.product.ProductOption;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.exception.OptionNotFoundException;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.exception.UserNotFoundException;
import gift.core.exception.ErrorCode;
import gift.core.exception.validation.InvalidArgumentException;
import gift.order.service.OrderAlarmGateway;
import gift.order.service.OrderServiceImpl;
import gift.order.service.OrderPointStrategy;
import gift.order.service.PointOperationSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductOptionRepository productOptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderAlarmGateway orderAlarmGateway;

    @Mock
    private PointOperationSupport pointOperationSupport;

    @Mock
    private OrderPointStrategy orderPointStrategy;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl(
                orderRepository,
                productOptionRepository,
                userRepository,
                pointOperationSupport,
                orderPointStrategy,
                orderAlarmGateway
        );
    }

    @Test
    void testOrderProduct() {
        Order order = Order.newOrder(1L, 1L, 100L, 1, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.of(ProductOption.of("test", 10)));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.orderProduct(order, "test");
        verify(productOptionRepository).save(anyLong(), any());
        verify(orderRepository).save(order);
        verify(orderAlarmGateway).sendAlarm("test", "test");
    }

    @Test
    @DisplayName("주문 시 기입하는 유저 ID가 존재하지 않는 유저의 ID일 때 예외가 발생한다.")
    void testOrderProductWhenUserDoesNotExist() {
        Order order = Order.newOrder(1L, 1L, 100L, 1, "test");

        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> orderService.orderProduct(order, "test"));
    }

    @Test
    @DisplayName("주문 시 기입하는 옵션 ID가 존재하지 않는 옵션의 ID일 때 예외가 발생한다.")
    void testOrderProductWhenOptionDoesNotExist() {
        Order order = Order.newOrder(1L, 1L, 100L, 1, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OptionNotFoundException.class, () -> orderService.orderProduct(order, "test"));
    }

    @Test
    @DisplayName("주문 시 기입하는 수량이 주문 가능 수량보다 클 때 예외가 발생한다.")
    void testOrderProductWhenQuantityIsGreaterThanOrderable() {
        Order order = Order.newOrder(1L, 1L, 100L, 100, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.of(ProductOption.of("test", 10)));

        assertThrows(InvalidArgumentException.class, () -> orderService.orderProduct(order, "test"));
    }

    @Test
    @DisplayName("주문 시 기입하는 수량이 0보다 작을 때 예외가 발생한다.")
    void testOrderProductWhenQuantityIsNegative() {
        Order order = Order.newOrder(1L, 1L, 100L, -1, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.of(ProductOption.of("test", 10)));

        assertThrows(InvalidArgumentException.class, () -> orderService.orderProduct(order, "test"));
    }

    @Test
    @DisplayName("주문 시 기입하는 포인트가 0보다 작을 때 예외가 발생한다.")
    void testOrderProductWhenPointIsNegative() {
        Order order = Order.newOrder(1L, 1L, -1L, 1, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.of(ProductOption.of("test", 10)));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderPointStrategy.calculatePoint(order)).thenThrow(new InvalidArgumentException(ErrorCode.NEGATIVE_POINT));

        assertThrows(InvalidArgumentException.class, () -> orderService.orderProduct(order, "test"));
    }

    @Test
    @DisplayName("주문 시 사용하려는 포인트가 가지고 있는 포인트보다 많을 때 발생한다.")
    void testOrderProductWhenPointIsGreaterThanRemainPoint() {
        Order order = Order.newOrder(1L, 1L, 100L, 1, "test");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(productOptionRepository.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productOptionRepository.findById(1L)).thenReturn(Optional.of(ProductOption.of("test", 10)));
        doThrow(new InvalidArgumentException(ErrorCode.POINT_NOT_ENOUGH)).when(pointOperationSupport).subtractPoint(anyLong(), anyLong());

        assertThrows(InvalidArgumentException.class, () -> orderService.orderProduct(order, "test"));
    }
}
