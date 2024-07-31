package gift.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Role;
import java.util.List;
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
    private OrderItemService orderItemService;

    @MockBean
    private MessageService messageService;

    private static final Member MEMBER = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.KAKAO);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option1 = new Option(1L, product, "사과맛", 90);
    private static final Option option2 = new Option(2L, product, "포도맛", 70);


    @Test
    void createAndSendMessage() {
        // given
        product.addOption(option1);
        product.addOption(option2);

        List<OrderItemRequest> orderItemRequests = List.of(
            new OrderItemRequest(1L, 1L, 10),
            new OrderItemRequest(1L, 2L, 30)
        );
        OrderRequest orderRequest = new OrderRequest(orderItemRequests, "Test Message", 180000);
        Order order = orderRequest.toOrder(MEMBER);
        OrderResponse expected = OrderResponse.from(orderRequest.toOrder(MEMBER));

        doNothing().when(orderItemService).create(any(Member.class), any(Order.class), any(List.class));
        given(orderJpaRepository.save(any(Order.class))).willReturn(order);
        given(messageService.sendMessageToMe(any(Member.class), any(OrderResponse.class))).willReturn("0");

        // when
        OrderResponse actual = orderService.createAndSendMessage(orderRequest, MEMBER);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}