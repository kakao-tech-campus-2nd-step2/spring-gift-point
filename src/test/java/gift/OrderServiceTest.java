package gift;

import gift.domain.*;
import gift.dto.OrderItemRequest;
import gift.dto.OrderRequest;
import gift.repository.*;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductService productService;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private KakaoService kakaoService;

    private Member member;
    private Product product;
    private Option option;
    private OrderItemRequest orderItemRequest;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "test@example.com", "password");
        product = new Product(1L, "Test Product", 1000L, "http://example.com/image.png", new Category("Test Category", "Red", "http://example.com/image.png", "Description"));
        option = new Option("Test Option", 10, product);

        orderItemRequest = new OrderItemRequest(1L, 1L, 2);

        orderRequest = new OrderRequest(Arrays.asList(orderItemRequest), "Happy Birthday!");
    }

    @Test
    @DisplayName("주문 성공 테스트")
    void testPlaceOrder_Success() throws Exception {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(productService.getProductById(any(Long.class))).thenReturn(product);
        when(productService.getOptionById(any(Long.class))).thenReturn(option);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        orderService.placeOrder(orderRequest, 1L);

        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(productService, times(1)).getProductById(any(Long.class));
        verify(productService, times(1)).getOptionById(any(Long.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kakaoService, times(1)).sendOrderMessage(any(Long.class), any(Order.class));
    }

    @Test
    @DisplayName("주문 실패 테스트 - 멤버 찾기 실패")
    void testPlaceOrder_MemberNotFound() throws Exception {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            orderService.placeOrder(orderRequest, 1L);
        });

        assertThat(exception.getMessage(), is(equalTo("Invalid member ID")));
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(kakaoService, times(0)).sendOrderMessage(any(Long.class), any(Order.class));
    }

    @Test
    @DisplayName("주문 실패 테스트 - 상품 찾기 실패")
    void testPlaceOrder_ProductNotFound() throws Exception {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(productService.getProductById(any(Long.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            orderService.placeOrder(orderRequest, 1L);
        });

        assertThat(exception.getMessage(), is(equalTo("Invalid product ID: 1")));
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(productService, times(1)).getProductById(any(Long.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(kakaoService, times(0)).sendOrderMessage(any(Long.class), any(Order.class));
    }

    @Test
    @DisplayName("주문 실패 테스트 - 옵션이 상품에 없음")
    void testPlaceOrder_OptionDoesNotBelongToProduct() throws Exception {
        Option otherOption = new Option("Other Option", 2,
                new Product(2L, "Other Product", 2000L, "http://example.com/image2.png", new Category("Other Category", "Blue", "http://example.com/image2.png", "Other Description")));

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(productService.getProductById(any(Long.class))).thenReturn(product);
        when(productService.getOptionById(any(Long.class))).thenReturn(otherOption);

        Exception exception = assertThrows(Exception.class, () -> {
            orderService.placeOrder(orderRequest, 1L);
        });

        assertThat(exception.getMessage(), is(equalTo("Option does not belong to the specified product")));
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(productService, times(1)).getProductById(any(Long.class));
        verify(productService, times(1)).getOptionById(any(Long.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(kakaoService, times(0)).sendOrderMessage(any(Long.class), any(Order.class));
    }
}
