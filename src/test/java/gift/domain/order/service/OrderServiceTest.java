package gift.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.Role;
import gift.domain.order.dto.MultipleOrderRequest;
import gift.domain.order.dto.MultipleOrderResponse;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.entity.OrderItem;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.OptionJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderJpaRepository orderJpaRepository;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    @MockBean
    private OptionJpaRepository optionJpaRepository;

    @MockBean
    private MessageService messageService;

    private static final Member member = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.KAKAO);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option1 = new Option(1L, product, "사과맛", 90);
    private static final Option option2 = new Option(2L, product, "포도맛", 70);


    @BeforeEach
    void setUp() {
        product.removeOptions();
        product.addOption(option1);
        product.addOption(option2);
    }

    @Test
    @DisplayName("여러 아이템 주문 테스트")
    void createMultipleAndSendMessage() {
        // given
        member.rechargePoint(162000);

        List<OrderItemRequest> orderItemRequests = List.of(
            new OrderItemRequest(1L, 10),
            new OrderItemRequest(2L, 30)
        );
        MultipleOrderRequest orderRequest = new MultipleOrderRequest(orderItemRequests, "Test Message");
        Order order = orderRequest.toOrder(member);
        MultipleOrderResponse expected = MultipleOrderResponse.from(orderRequest.toOrder(member));


        given(productJpaRepository.findByOptionId(anyLong())).willReturn(Optional.of(product));
        doNothing().when(wishlistJpaRepository).deleteByMemberAndProduct(any(Member.class), any(Product.class));
        given(optionJpaRepository.findByIdWithOptimisticLock(eq(1L))).willReturn(Optional.of(option1));
        given(optionJpaRepository.findByIdWithOptimisticLock(eq(2L))).willReturn(Optional.of(option2));

        given(orderJpaRepository.save(any(Order.class))).willReturn(order);
        given(messageService.sendMessageToMe(any(Member.class), any(MultipleOrderResponse.class))).willReturn("0");

        // when
        MultipleOrderResponse actual = orderService.createMultipleAndSendMessage(orderRequest, member);

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(member.getPoint()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("주문 생성, 할인 테스트")
    void create_One_and_discount() {
        // given
        member.rechargePoint(70000);
        OrderRequest orderRequest = new OrderRequest(1L, 15, "5만원 넘음~");
        Order order = orderRequest.toOrder(member);
        OrderItem orderItem = new OrderItem(1L, order, product, option1, 15);
        order.addOrderItem(orderItem);
        order.addOriginalPrice(product.getPrice() * orderRequest.quantity());

        given(productJpaRepository.findByOptionId(anyLong())).willReturn(Optional.of(product));
        doNothing().when(wishlistJpaRepository).deleteByMemberAndProduct(any(Member.class), any(Product.class));
        given(optionJpaRepository.findByIdWithOptimisticLock(eq(1L))).willReturn(Optional.of(option1));

        given(orderJpaRepository.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        OrderResponse response = orderService.createOne(orderRequest, member);

        // then
        assertAll(
            () -> assertThat(response.finalPrice()).isEqualTo(60750),
            () -> assertThat(member.getPoint()).isEqualTo(9250)
        );
    }
}