package gift.order.application;

import gift.auth.KakaoService;
import gift.category.domain.Category;
import gift.discount.domain.DiscountPolicy;
import gift.exception.type.NotFoundException;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import gift.member.domain.OauthProvider;
import gift.option.application.OptionService;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.order.application.command.OrderCreateCommand;
import gift.order.application.response.OrderSaveServiceResponse;
import gift.order.domain.Order;
import gift.order.domain.OrderRepository;
import gift.product.domain.Product;
import gift.wishlist.domain.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KakaoService kakaoService;

    @Mock
    @Qualifier("percentageDiscountPolicy")
    private DiscountPolicy percentageDiscountPolicy;

    @Mock
    @Qualifier("amountDiscountPolicy")
    private DiscountPolicy amountDiscountPolicy;

    private OrderService orderService;
    private OrderCreateCommand command;
    private Long memberId;
    private Option option;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberId = 1L;
        option = new Option(1L, "name", 6);
        option.setProduct(new Product(1L, "name", 10000, "imageUrl", new Category(1L, "name", "color", "description", "imageUrl")));
        member = new Member(memberId, "test@example.com", "password", OauthProvider.COMMON, 12345L);

        command = new OrderCreateCommand(option.getId(), 6, "Order Message");
    }

    @Test
    void 유효한_주문을_저장_성공_퍼센트_할인() {
        // Given
        orderService = new OrderService(orderRepository, optionRepository, optionService, wishlistRepository, memberRepository, kakaoService, percentageDiscountPolicy);
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(percentageDiscountPolicy.calculateFinalAmount(anyInt())).thenReturn(54000);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderSaveServiceResponse response = orderService.save(command, memberId);

        // Then
        assertNotNull(response);
        assertEquals(60000, response.originalPrice());
        assertEquals(54000, response.finalPrice());

        verify(optionRepository, times(1)).findById(command.optionId());
        verify(optionService, times(1)).subtractOptionQuantity(command.toOptionSubtractQuantityCommand());
        verify(wishlistRepository, times(1)).findAllByMemberId(memberId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(kakaoService, times(1)).sendOrderMessage(eq(member.getKakaoId()), any(), any());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 유효한_주문을_저장_성공_금액_할인() {
        // Given
        orderService = new OrderService(orderRepository, optionRepository, optionService, wishlistRepository, memberRepository, kakaoService, amountDiscountPolicy);
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(amountDiscountPolicy.calculateFinalAmount(anyInt())).thenReturn(58000);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderSaveServiceResponse response = orderService.save(command, memberId);

        // Then
        assertNotNull(response);
        assertEquals(60000, response.originalPrice());
        assertEquals(58000, response.finalPrice());

        verify(optionRepository, times(1)).findById(command.optionId());
        verify(optionService, times(1)).subtractOptionQuantity(command.toOptionSubtractQuantityCommand());
        verify(wishlistRepository, times(1)).findAllByMemberId(memberId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(kakaoService, times(1)).sendOrderMessage(eq(member.getKakaoId()), any(), any());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 옵션을_찾을_수_없을_때_예외_발생() {
        // Given
        orderService = new OrderService(orderRepository, optionRepository, optionService, wishlistRepository, memberRepository, kakaoService, percentageDiscountPolicy);
        when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> orderService.save(command, memberId));

        verify(optionRepository, times(1)).findById(command.optionId());
        verify(optionService, times(0)).subtractOptionQuantity(any());
        verify(wishlistRepository, times(0)).findAllByMemberId(anyLong());
        verify(memberRepository, times(0)).findById(anyLong());
        verify(kakaoService, times(0)).sendOrderMessage(anyLong(), any(), any());
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}
