package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import gift.exception.option.OptionsQuantityException;
import gift.model.BaseEntity;
import gift.model.Category;
import gift.model.Member;
import gift.model.Options;
import gift.model.Order;
import gift.model.Product;
import gift.model.Role;
import gift.model.Wish;
import gift.repository.MemberRepository;
import gift.repository.OptionsRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.request.OrderRequest;
import gift.response.order.CreatedOrderResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OptionsRepository optionsRepository;
    @MockBean
    private OptionsService optionsService;
    @MockBean
    private WishRepository wishRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private KakaoMessageService kakaoMessageService;

    private Member member;
    private Product product;
    private Category category;
    private Options option;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "234242@kakao.com", "OAUTH2", Role.ROLE_USER, 1000);
        category = new Category(1L, "카테고리");
        product = new Product(1L, "상품", 1000, "http://image.com", category);
        option = new Options(1L, "옵션", 10, product);
    }

    @DisplayName("주문 테스트")
    @Test
    void order() {
        //given
        String oAuthToken = "oAuthToken";
        Integer orderQuantity = 1;
        Integer usedPoint = 250;
        String message = "message";
        Order savedOrder = new Order(1L, 1L,
            option, orderQuantity, message, LocalDateTime.now(), LocalDateTime.now());
        Wish wish = new Wish(member, product, 1);
        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1, message, usedPoint);

        given(optionsService.getOption(any(Long.class)))
            .willReturn(option);
        given(memberRepository.findById(any(Long.class)))
            .willReturn(Optional.ofNullable(member));
        doNothing().when(optionsService).subtractQuantity(any(Long.class),
            any(Integer.class), any(Long.class));
        doNothing().when(kakaoMessageService).sendMessageToMe(any(String.class),
            any(String.class));
        given(wishRepository.findByMemberIdAndProductId(any(Long.class), any(Long.class)))
            .willReturn(Optional.of(wish));
        //doCallRealMethod().when(member).subPoint(any(Integer.class));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);
        //doCallRealMethod().when(member).addPoint(any(Integer.class));
        //when
        CreatedOrderResponse returendResponse = orderService.makeOrder(member.getId(),
            oAuthToken, orderRequest);

        //then
        then(optionsService).should().getOption(any(Long.class));
        then(orderRepository).should().save(any(Order.class));
        then(optionsService).should().subtractQuantity(any(Long.class),
            any(Integer.class), any(Long.class));
        then(wishRepository).should().findByMemberIdAndProductId(any(Long.class), any(Long.class));
        then(kakaoMessageService).should().sendMessageToMe(any(String.class), any(String.class));
        assertThat(member.getPoint()).isEqualTo(850); // 1000 - 250 + 100(가격 1000/10)
        assertThat(returendResponse.usedPoint()).isEqualTo(usedPoint);
    }

    @DisplayName("주문 실패 테스트 - 재고 부족")
    @Test
    void failOrder() {
        //given
        Integer orderQuantity = 1;
        String message = "message";
        Order order = new Order(member.getId(), option,
            orderQuantity, message);
        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1, message, 100);
        given(optionsService.getOption(any(Long.class)))
            .willReturn(option);
        given(memberRepository.findById(any(Long.class)))
            .willReturn(Optional.ofNullable(member));
        doThrow(OptionsQuantityException.class).when(optionsService)
            .subtractQuantity(any(Long.class),
                any(Integer.class), any(Long.class));

        //when //then
        Assertions.assertThatThrownBy(
                () -> orderService.addOrder(member.getId(), orderRequest))
            .isInstanceOf(OptionsQuantityException.class);
    }
}