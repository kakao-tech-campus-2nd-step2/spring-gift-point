package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.User.UserResponse;
import gift.TestUtil;
import gift.domain.*;
import gift.exception.LogicalException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private KakaoService kakaoUserService;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("saveTest")
    void test1(){
        // given
        OrderRequest orderRequest = new OrderRequest(1L, 20L, "선물합니다");
        Order order = new Order(
                orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage());
        TestUtil.setDate(order);
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> order);
        // when
        OrderResponse savedOrder = orderService.save(orderRequest);
        // then
        Assertions.assertThat(savedOrder.getMessage()).isEqualTo("선물합니다");
        Assertions.assertThat(savedOrder.getQuantity()).isEqualTo(20L);
        Assertions.assertThat(savedOrder.getOptionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("NormalOrderTest")
    void test2() throws JsonProcessingException {
        // given
        ArgumentCaptor<Long> captor_id = ArgumentCaptor.forClass(Long.class);

        User user = new User("user", "user@email.com", "aaaa");
        user.insertToken("01010101");
        TestUtil.setId(user, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, 20L, "선물합니다");
        UserResponse userResponse = new UserResponse(user);
        Long productId = 1L;

        Option option = new Option("[1] 기본", 200L);
        given(optionRepository.findById(captor_id.capture())).willAnswer(invocation -> Optional.of(option));

        Category category = new Category("물품");
        Product product = new Product("product", 4000, "none", category);
        TestUtil.setId(product, 1L);
        product.addOption(option);
        given(productRepository.findById(productId)).willAnswer(invocation -> Optional.of(product));

        given(wishListRepository.existsByUserIdAndProductId(anyLong(), anyLong())).willAnswer(invocation -> false);

        Order order = new Order(1L, 20L, "선물합니다");
        TestUtil.setId(order, 1L);
        TestUtil.setDate(order);
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> order);

        // when
        OrderResponse savedOrder = orderService.order(orderRequest, userResponse, productId);

        // then
        Assertions.assertThat(savedOrder.getOptionId()).isEqualTo(orderRequest.getOptionId());
        Assertions.assertThat(savedOrder.getQuantity()).isEqualTo(orderRequest.getQuantity());
        Assertions.assertThat(savedOrder.getMessage()).isEqualTo(orderRequest.getMessage());
    }

    @Test
    @DisplayName("AbnormalOrderTest1")
    void test3(){
        // given
        ArgumentCaptor<Long> captor_id = ArgumentCaptor.forClass(Long.class);

        User user = new User("user", "user@email.com", "aaaa");
        TestUtil.setId(user, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, 20L, "선물합니다");
        UserResponse userResponse = new UserResponse(user);
        Long productId = 1L;

        // then
        Assertions.assertThatThrownBy((()->orderService.order(orderRequest, userResponse, productId)))
                .isInstanceOf( NoSuchFieldError.class)
                .hasMessage("카카오 유저만 구매할 수 있습니다!");
    }

    @Test
    @DisplayName("AbnormalOrderTest2")
    void test4(){
        // given
        ArgumentCaptor<Long> captor_id = ArgumentCaptor.forClass(Long.class);

        User user = new User("user", "user@email.com", "aaaa");
        user.insertToken("01010101");
        TestUtil.setId(user, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, 201L, "선물합니다");
        UserResponse userResponse = new UserResponse(user);
        Long productId = 1L;

        Option option = new Option("[1] 기본", 200L);
        given(optionRepository.findById(captor_id.capture())).willAnswer(invocation -> Optional.of(option));

        // then
        Assertions.assertThatThrownBy((()->orderService.order(orderRequest, userResponse, productId)))
                .isInstanceOf(LogicalException.class)
                .hasMessage("수량보다 많은 수는 주문할 수 없습니다!");
    }
}
