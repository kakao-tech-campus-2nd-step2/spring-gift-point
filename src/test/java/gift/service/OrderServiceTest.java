package gift.service;

import gift.domain.*;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderPageResponse;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.exception.*;
import gift.repository.member.MemberSpringDataJpaRepository;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static gift.domain.LoginType.KAKAO;
import static gift.domain.LoginType.NORMAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderSpringDataJpaRepository orderRepository;

    @Mock
    private WishlistSpringDataJpaRepository wishlistRepository;

    @Mock
    private MemberSpringDataJpaRepository memberRepository;

    @Mock
    private ProductSpringDataJpaRepository productRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private KakaoMessageService kakaoMessageService;

    @InjectMocks
    private OrderService orderService;

    private static final String INSUFFICIENT_POINTS = "포인트가 부족합니다.";
    private static final String RECEIPT_REQUIRED_PHONE = "현금 영수증을 받기 전에 전화번호를 입력하세요.";
    private static final String EXCESSIVE_POINTS = "포인트가 주문 가격을 초과했습니다.";
    private static final double PRICE_IF_DISCOUNT = 0.9;

    @Test
    void 일반_로그인_유형으로_주문_생성_성공() {
        Long memberId = 1L;
        Long productId = 1L;
        Long optionId = 1L;
        int orderPrice = 1200;
        int pointUsed = 100;
        int memberPoints = 1500;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        when(mockMember.getPoint()).thenReturn(memberPoints);
        when(mockMember.getLoginType()).thenReturn(NORMAL);

        OrderRequest mockOrderRequest = mock(OrderRequest.class);
        when(mockOrderRequest.getProductId()).thenReturn(productId);
        when(mockOrderRequest.getQuantity()).thenReturn(1);
        when(mockOrderRequest.getOptionId()).thenReturn(optionId);
        when(mockOrderRequest.getPoint()).thenReturn(pointUsed);
        when(mockOrderRequest.getPhone()).thenReturn("000-0000-0000");
        when(mockOrderRequest.isReceipt()).thenReturn(true);

        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockProduct.getPrice()).thenReturn(orderPrice);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(optionService.getProductIdByOptionId(optionId)).thenReturn(productId);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TokenAuth mockTokenAuth = mock(TokenAuth.class);
        when(mockTokenAuth.getMember()).thenReturn(mockMember);

        OrderResponse orderResponse = orderService.createOrder(mockTokenAuth, mockOrderRequest);

        assertNotNull(orderResponse);
        assertEquals(memberId, orderResponse.getMemberId());
        verify(memberRepository, times(1)).subtractPoints(memberId, pointUsed);
        verify(optionService, times(1)).subtractOptionQuantity(productId, optionId, 1);
        verify(wishlistRepository, times(1)).deleteByMemberIdAndProductId(memberId, productId);
        verify(memberRepository, times(1)).addPoints(memberId, 110);
        verify(kakaoMessageService, never()).sendOrderMessage(anyString(), any(Order.class));
    }

    @Test
    void 카카오_로그인_유형으로_주문_생성_성공() {
        Long memberId = 1L;
        Long productId = 1L;
        Long optionId = 1L;
        int orderPrice = 1200;
        int pointUsed = 100;
        int memberPoints = 1500;
        String kakaoToken = "test-token";

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        when(mockMember.getPoint()).thenReturn(memberPoints);
        when(mockMember.getLoginType()).thenReturn(KAKAO);

        OrderRequest mockOrderRequest = mock(OrderRequest.class);
        when(mockOrderRequest.getProductId()).thenReturn(productId);
        when(mockOrderRequest.getQuantity()).thenReturn(1);
        when(mockOrderRequest.getOptionId()).thenReturn(optionId);
        when(mockOrderRequest.getPoint()).thenReturn(pointUsed);
        when(mockOrderRequest.getPhone()).thenReturn("000-0000-0000");
        when(mockOrderRequest.isReceipt()).thenReturn(true);

        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockProduct.getPrice()).thenReturn(orderPrice);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(optionService.getProductIdByOptionId(optionId)).thenReturn(productId);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TokenAuth mockTokenAuth = mock(TokenAuth.class);
        when(mockTokenAuth.getMember()).thenReturn(mockMember);
        when(mockTokenAuth.getAccessToken()).thenReturn(kakaoToken);

        OrderResponse orderResponse = orderService.createOrder(mockTokenAuth, mockOrderRequest);

        assertNotNull(orderResponse);
        assertEquals(memberId, orderResponse.getMemberId());
        verify(memberRepository, times(1)).subtractPoints(memberId, pointUsed);
        verify(optionService, times(1)).subtractOptionQuantity(productId, optionId, 1);
        verify(wishlistRepository, times(1)).deleteByMemberIdAndProductId(memberId, productId);
        verify(memberRepository, times(1)).addPoints(memberId, 110);
        verify(kakaoMessageService, times(1)).sendOrderMessage(eq(kakaoToken), any(Order.class));
    }

    @Test
    void 포인트_부족으로_인한_주문_생성_실패() {
        Long optionId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getPoint()).thenReturn(50); // 포인트 부족

        OrderRequest mockOrderRequest = mock(OrderRequest.class);
        when(mockOrderRequest.getQuantity()).thenReturn(2);
        when(mockOrderRequest.getOptionId()).thenReturn(optionId);
        when(mockOrderRequest.getPoint()).thenReturn(100);
        when(mockOrderRequest.getPhone()).thenReturn("000-0000-0000");
        when(mockOrderRequest.isReceipt()).thenReturn(true);

        TokenAuth mockTokenAuth = mock(TokenAuth.class);
        when(mockTokenAuth.getMember()).thenReturn(mockMember);

        InsufficientPointsException exception = assertThrows(InsufficientPointsException.class, () -> orderService.createOrder(mockTokenAuth, mockOrderRequest));
        assertEquals(INSUFFICIENT_POINTS, exception.getMessage());
    }

    @Test
    void 전화번호_누락으로_인한_영수증_요청_실패() {
        Long optionId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getPoint()).thenReturn(400);

        OrderRequest mockOrderRequest = mock(OrderRequest.class);
        when(mockOrderRequest.getQuantity()).thenReturn(2);
        when(mockOrderRequest.getOptionId()).thenReturn(optionId);
        when(mockOrderRequest.getPoint()).thenReturn(100);
        when(mockOrderRequest.getPhone()).thenReturn(null);
        when(mockOrderRequest.isReceipt()).thenReturn(true);

        TokenAuth mockTokenAuth = mock(TokenAuth.class);
        when(mockTokenAuth.getMember()).thenReturn(mockMember);

        ReceiptRequiredPhoneException exception = assertThrows(ReceiptRequiredPhoneException.class, () -> orderService.createOrder(mockTokenAuth, mockOrderRequest));
        assertEquals(RECEIPT_REQUIRED_PHONE, exception.getMessage());
    }

    @Test
    void 포인트가_주문_가격을_초과하는_경우_실패() {
        Long productId = 1L;
        Long optionId = 1L;
        int orderPrice = 1000;
        int pointUsed = 1200;
        int memberPoints = 1500;

        Member mockMember = mock(Member.class);
        when(mockMember.getPoint()).thenReturn(memberPoints);

        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockProduct.getPrice()).thenReturn(orderPrice);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        when(optionService.getProductIdByOptionId(optionId)).thenReturn(productId);

        OrderRequest mockOrderRequest = mock(OrderRequest.class);
        when(mockOrderRequest.getQuantity()).thenReturn(1);
        when(mockOrderRequest.getOptionId()).thenReturn(optionId);
        when(mockOrderRequest.getPoint()).thenReturn(pointUsed);
        when(mockOrderRequest.getPhone()).thenReturn("000-0000-0000");
        when(mockOrderRequest.isReceipt()).thenReturn(true);

        TokenAuth mockTokenAuth = mock(TokenAuth.class);
        when(mockTokenAuth.getMember()).thenReturn(mockMember);

        ExcessivePointsException exception = assertThrows(ExcessivePointsException.class, () -> orderService.createOrder(mockTokenAuth, mockOrderRequest));
        assertEquals(EXCESSIVE_POINTS, exception.getMessage());
    }

    @Test
    void 오만원_이상_주문_가격_계산_성공() {
        Long productId = 1L;
        Long optionId = 1L;
        int quantity = 2;
        int price = 51000;
        int expectedPrice = (int) (price * quantity * PRICE_IF_DISCOUNT);

        PriceRequest mockPriceRequest = mock(PriceRequest.class);
        when(mockPriceRequest.getProductId()).thenReturn(productId);
        when(mockPriceRequest.getOptionId()).thenReturn(optionId);
        when(mockPriceRequest.getQuantity()).thenReturn(quantity);

        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockProduct.getPrice()).thenReturn(price);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(optionService.getProductIdByOptionId(optionId)).thenReturn(productId);

        PriceResponse priceResponse = orderService.getOrderPrice(mockPriceRequest);

        assertNotNull(priceResponse);
        assertEquals(expectedPrice, priceResponse.getPrice());
        verify(productRepository, times(1)).findById(productId);
        verify(optionService, times(1)).getProductIdByOptionId(optionId);
    }

    @Test
    void 회원ID로_주문_조회_성공() {
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(1L);

        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(mockOrder));
        when(orderRepository.findByMemberId(memberId, pageable)).thenReturn(ordersPage);

        OrderPageResponse orderPageResponse = orderService.getOrdersByMemberId(memberId, pageable);

        assertNotNull(orderPageResponse);
        assertEquals(1, orderPageResponse.getContent().size());
        assertEquals(1L, orderPageResponse.getContent().get(0).getId());
        verify(orderRepository, times(1)).findByMemberId(memberId, pageable);
    }
}
