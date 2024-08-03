package gift.service;

import gift.domain.*;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.exception.*;
import gift.repository.member.MemberSpringDataJpaRepository;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderSpringDataJpaRepository orderRepository;

    @Mock
    private ProductSpringDataJpaRepository productRepository;

    @Mock
    private WishlistSpringDataJpaRepository wishlistRepository;

    @Mock
    private MemberSpringDataJpaRepository memberRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_success() {
        // Given
        TokenAuth tokenAuth = new TokenAuth();
        Member member = new Member("test@example.com", "password", LoginType.NORMAL);
        member.setId(1L);
        tokenAuth.setMember(member);

        OrderRequest orderRequest = new OrderRequest(1L, 2, "Test Message", 1L, 100, "000-0000-0000", true);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(optionService.getProductIdByOptionId(1L)).thenReturn(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(1L, "Test Product", 1000)));
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(kakaoMessageService.sendOrderMessage(anyString(), any(Order.class))).thenReturn(true);

        // When
        OrderResponse orderResponse = orderService.createOrder(tokenAuth, orderRequest);

        // Then
        assertNotNull(orderResponse);
        assertEquals(1L, orderResponse.getMemberId());
        verify(memberRepository, times(1)).subtractPoints(1L, 100);
        verify(optionService, times(1)).subtractOptionQuantity(1L, 1L, 2);
        verify(wishlistRepository, times(1)).deleteByMemberIdAndProductId(1L, 1L);
        verify(kakaoMessageService, times(1)).sendOrderMessage(anyString(), any(Order.class));
    }

    @Test
    void createOrder_insufficientPoints() {
        // Given
        TokenAuth tokenAuth = new TokenAuth();
        Member member = new Member("test@example.com", "password", LoginType.NORMAL);
        member.setId(1L);
        member.setPoint(50);
        tokenAuth.setMember(member);

        OrderRequest orderRequest = new OrderRequest(1L, 2, "Test Message", 1L, 100, "000-0000-0000", true);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // When & Then
        assertThrows(InsufficientPointsException.class, () -> orderService.createOrder(tokenAuth, orderRequest));
    }
}
