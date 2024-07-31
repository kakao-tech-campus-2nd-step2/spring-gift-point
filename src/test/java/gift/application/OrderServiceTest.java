package gift.application;

import gift.kakao.client.KakaoClient;
import gift.member.application.MemberService;
import gift.member.entity.KakaoTokenInfo;
import gift.member.entity.Member;
import gift.order.application.OrderService;
import gift.order.dao.OrderRepository;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import gift.order.entity.Order;
import gift.product.application.OptionService;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.wishlist.application.WishesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import testFixtures.MemberFixture;
import testFixtures.OptionFixture;
import testFixtures.ProductFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OptionService optionService;

    @Mock
    private WishesService wishesService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private KakaoClient kakaoClient;

    @Test
    @DisplayName("상품 주문 기능 테스트")
    void order() {
        Product product = ProductFixture.createProduct("product", null);
        Member member = new Member(
                "test@email.com",
                "password",
                new KakaoTokenInfo(
                        "token",
                        LocalDateTime.now()
                                     .plusSeconds(100000L),
                        "refresh-token"
                )
        );
        Option option = OptionFixture.createOption("옵션", product);
        Long memberId = 1L;
        OrderRequest request = new OrderRequest(2L, 1, "message");
        given(optionService.getOptionById(anyLong()))
                .willReturn(option);
        given(memberService.getMemberByIdOrThrow(anyLong()))
                .willReturn(member);
        given(orderRepository.save(any()))
                .willReturn(new Order(request.message(), option, member));

        OrderResponse response = orderService.order(memberId, request);

        assertThat(response.message()).isEqualTo(request.message());
        verify(optionService).getOptionById(request.optionId());
        verify(optionService).subtractQuantity(any(), eq(request.quantity()));
        verify(wishesService).removeWishIfPresent(eq(memberId), any());
        verify(memberService).getMemberByIdOrThrow(memberId);
        verify(orderRepository).save(any());
    }

    @Test
    @DisplayName("주문 목록 조회 기능 테스트")
    void getOrders() {
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order(
                "message1",
                OptionFixture.createOption("옵션", null),
                MemberFixture.createMember("testuser@email.com")
        );
        Order order2 = new Order(
                "message2",
                OptionFixture.createOption("옵션", null),
                MemberFixture.createMember("testuser@email.com")
        );
        orderList.add(order1);
        orderList.add(order2);
        Page<Order> orders = new PageImpl<>(orderList);
        given(orderRepository.findAll(orders.getPageable()))
                .willReturn(orders);

        Page<OrderResponse> pagedOrders = orderService.getPagedOrders(orders.getPageable());

        verify(orderRepository).findAll(orders.getPageable());
        assertThat(pagedOrders.getTotalElements()).isEqualTo(orderList.size());
    }

}