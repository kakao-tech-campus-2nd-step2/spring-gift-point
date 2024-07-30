package gift;

import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import gift.service.ProductOptionService;
import gift.service.WishService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductOptionService productOptionService;

    @Mock
    private WishService wishService;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @Mock
    private HttpSession session;

    private Member createTestMember() {
        return new Member("test@example.com", "password");
    }

    private ProductOption createTestProductOption() {
        return new ProductOption(1L, "Option 1", 10);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_success() {
        Member member = createTestMember();
        ProductOption productOption = createTestProductOption();

        when(productOptionService.findProductOptionById(1L)).thenReturn(productOption);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(session.getAttribute("accessToken")).thenReturn("mockAccessToken");

        Order order = orderService.createOrder(1L, 2, "Please handle with care", member);

        assertNotNull(order);
        assertEquals(1L, order.getProductOption().getId());
        assertEquals(2, order.getQuantity());
        assertEquals("Please handle with care", order.getMessage());
        assertEquals(member, order.getMember());
        assertNotNull(order.getOrderDateTime());

        verify(productOptionService).subtractProductOptionQuantity(1L, 2);
        verify(orderRepository).save(order);
        verify(wishService).deleteWishByProductOptionIdAndMemberId(1L, member.getId());
        verify(kakaoMessageService).sendMessage(eq("mockAccessToken"), anyString());
    }

    @Test
    void createOrder_invalidProductOption() {
        Member member = createTestMember();

        when(productOptionService.findProductOptionById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(1L, 2, "Please handle with care", member);
        });

        assertEquals("Invalid product option", exception.getMessage());
        verify(productOptionService, never()).subtractProductOptionQuantity(anyLong(), anyInt());
        verify(orderRepository, never()).save(any());
        verify(wishService, never()).deleteWishByProductOptionIdAndMemberId(anyLong(), anyLong());
        verify(kakaoMessageService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void createOrder_noAccessToken() {
        Member member = createTestMember();
        ProductOption productOption = createTestProductOption();

        when(productOptionService.findProductOptionById(1L)).thenReturn(productOption);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(session.getAttribute("accessToken")).thenReturn(null);

        Order order = orderService.createOrder(1L, 2, "Please handle with care", member);

        assertNotNull(order);
        assertEquals(1L, order.getProductOption().getId());
        assertEquals(2, order.getQuantity());
        assertEquals("Please handle with care", order.getMessage());
        assertEquals(member, order.getMember());
        assertNotNull(order.getOrderDateTime());

        verify(productOptionService).subtractProductOptionQuantity(1L, 2);
        verify(orderRepository).save(order);
        verify(wishService).deleteWishByProductOptionIdAndMemberId(1L, member.getId());
        verify(kakaoMessageService, never()).sendMessage(anyString(), anyString());
    }
}
