package gift.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;

import gift.member.MemberFixture;
import gift.member.dto.MemberResDto;
import gift.member.entity.Member;
import gift.member.points.PercentageBasedPointsStrategy;
import gift.member.points.PointsStrategy;
import gift.member.service.MemberService;
import gift.option.OptionFixture;
import gift.option.entity.Option;
import gift.option.service.OptionService;
import gift.order.OrderFixture;
import gift.order.dto.OrderReqDto;
import gift.order.dto.OrderResDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.ProductFixture;
import gift.product.entity.Product;
import gift.wishlist.WishListFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스 테스트")
class OrderServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private OptionService optionService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 조회 성공")
    void getOrders_success() {
        //given
        Member member = MemberFixture.createMember();
        given(memberService.findMemberByIdOrThrow(any())).willReturn(member);

        List<Option> options = List.of(
                OptionFixture.createOption("옵션1", 10),
                OptionFixture.createOption("옵션2", 20)
        );

        List<Order> orders = List.of(
                OrderFixture.createOrder(member, options.get(0), 5, "메시지1"),
                OrderFixture.createOrder(member, options.get(1), 7, "메시지2")
        );

        given(orderRepository.findAllByMember(eq(member), any())).willReturn(
                new PageImpl<>(orders, Pageable.ofSize(2), 2)
        );

        //when
        Page<OrderResDto> orderPage = orderService.getOrders(new MemberResDto(member), Pageable.ofSize(2));
        List<OrderResDto> orderResDtos = orderPage.getContent();

        //then
        assertThat(orderPage.getTotalElements()).isEqualTo(2);
        assertThat(orderPage.getTotalPages()).isOne();

        assertThatList(orderResDtos).hasSize(2);
        assertThatList(orderResDtos).extracting(OrderResDto::quantity)
                .containsExactly(5, 7);
        assertThatList(orderResDtos).extracting(OrderResDto::message)
                .containsExactly("메시지1", "메시지2");
        assertThatList(orderResDtos).extracting(OrderResDto::orderDateTime)
                .containsExactly(orders.get(0).getOrderDateTime(), orders.get(1).getOrderDateTime());
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() {
        //given
        Option option = mock(Option.class);
        given(option.getId()).willReturn(1L);
        given(option.getName()).willReturn("옵션");
        given(option.getQuantity()).willReturn(10);

        Product product = ProductFixture.createProduct("상품", 10000);
        product.getOptions().add(option);

        given(option.getProduct()).willReturn(product);

        Member member = MemberFixture.createMember();
        Integer beforePoints = member.getPoints();
        member.getWishLists().add(WishListFixture.createWishList(member, product));

        PointsStrategy pointsStrategy = new PercentageBasedPointsStrategy();

        given(optionService.findByIdOrThrow(any())).willReturn(option);

        given(memberService.findMemberByIdOrThrow(any())).willReturn(member);

        willAnswer(invocationOnMock -> {
            Option givenOption = invocationOnMock.getArgument(0);
            int givenQuantity = invocationOnMock.getArgument(1);

            assertThat(givenOption.getName()).isEqualTo("옵션");
            assertThat(givenOption.getQuantity()).isEqualTo(10);
            assertThat(givenQuantity).isEqualTo(5);

            givenOption.subtractQuantity(givenQuantity);
            return null;
        }).given(optionService).subtractQuantity(any(), anyInt());

        willAnswer(invocationOnMock -> {
            Member givenMember = invocationOnMock.getArgument(0);
            int givenPoints = invocationOnMock.getArgument(1);
            int givenPrice = invocationOnMock.getArgument(2);

            assertThat(givenMember).isEqualTo(member);
            assertThat(givenPoints).isEqualTo(1000);
            assertThat(givenPrice).isEqualTo(50000);

            givenMember.usePoints(givenPoints);
            givenMember.addPoints(pointsStrategy.calculatePointsToAdd(givenPrice));
            return null;
        }).given(memberService).processOrderPoints(any(), anyInt(), anyInt());

        Order order = OrderFixture.createOrder(member, option, 5, "메시지");
        given(orderRepository.save(any())).willReturn(order);

        Integer pointsToUse = 1000;
        OrderReqDto orderReqDto = OrderFixture.createOrderReqDto(1L, 5, pointsToUse, "메시지");

        //when
        OrderResDto orderResDto = orderService.createOrder(new MemberResDto(member), orderReqDto);

        //then
        assertThat(orderResDto.quantity()).isEqualTo(5);
        assertThat(orderResDto.message()).isEqualTo("메시지");
        assertThat(orderResDto.orderDateTime()).isEqualTo(order.getOrderDateTime());

        // 위시리스트에서 삭제되었는지 확인
        assertThat(member.getWishLists()).isEmpty();

        // 주문 포인트 차감 확인
        assertThat(member.getPoints()).isEqualTo(
                beforePoints - pointsToUse + pointsStrategy.calculatePointsToAdd(product.getPrice() * orderReqDto.quantity())
        );

        verify(optionService).subtractQuantity(option, 5);
        verify(option).subtractQuantity(5);
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrder_success() {
        //given
        Member member = mock(Member.class);
        given(member.getId()).willReturn(1L);

        Option option = mock(Option.class);
        willAnswer(invocationOnMock -> {
            int givenQuantity = invocationOnMock.getArgument(0);
            assertThat(givenQuantity).isEqualTo(5);
            return null;
        }).given(option).addQuantity(anyInt());

        Order order = OrderFixture.createOrder(member, option, 5, "메시지");

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        willAnswer(invocationOnMock -> {
            Option givenOption = invocationOnMock.getArgument(0);
            int givenQuantity = invocationOnMock.getArgument(1);

            assertThat(givenOption).isEqualTo(option);
            assertThat(givenQuantity).isEqualTo(5);

            givenOption.addQuantity(givenQuantity);
            return null;
        }).given(optionService).addQuantity(any(), anyInt());

        //when
        orderService.cancelOrder(new MemberResDto(member), order.getId());

        //then
        verify(orderRepository).delete(order);
        verify(optionService).addQuantity(option, 5);
        verify(option).addQuantity(5);
    }
}
