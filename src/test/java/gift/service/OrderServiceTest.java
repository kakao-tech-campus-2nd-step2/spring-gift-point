package gift.service;

import gift.domain.Order;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderSpringDataJpaRepository orderRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private WishlistSpringDataJpaRepository wishlistRepository;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        Long optionId = 1L;
        Integer quantity = 2;
        Long productId = 1L;
        Long receiveMemberId = 1L;
        OrderRequest orderRequest = new OrderRequest(optionId, quantity, "테스트 메시지", receiveMemberId);

        when(optionService.getProductIdByOptionId(optionId)).thenReturn(productId);
        doNothing().when(optionService).subtractOptionQuantity(productId, optionId, quantity);
        doNothing().when(wishlistRepository).deleteByMemberIdAndProductId(receiveMemberId, productId);
        doNothing().when(kakaoMessageService).sendOrderMessage(eq("token"), any(Order.class));

        Order orderMock = mock(Order.class);

        when(orderRepository.save(any(Order.class))).thenReturn(orderMock);

        OrderResponse orderResponse = orderService.createOrder("token", orderRequest);

        verify(optionService, times(1)).subtractOptionQuantity(eq(productId), eq(optionId), eq(quantity));
        verify(wishlistRepository, times(1)).deleteByMemberIdAndProductId(eq(receiveMemberId), eq(productId));
        verify(kakaoMessageService, times(1)).sendOrderMessage(eq("token"), any(Order.class));

        assertNotNull(orderResponse);
        assertEquals(orderRequest.getOptionId(), orderResponse.getOptionId());
        assertEquals(orderRequest.getQuantity(), orderResponse.getQuantity());
        assertEquals(orderRequest.getMessage(), orderResponse.getMessage());
        assertEquals(receiveMemberId, orderResponse.getReceiveMemberId());
    }
}
