package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.api.kakaoMessage.KakaoMessageClient;
import gift.category.model.Category;
import gift.common.auth.LoginMemberDto;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.member.oauth.OauthTokenRepository;
import gift.member.oauth.model.OauthToken;
import gift.option.OptionRepository;
import gift.option.model.Option;
import gift.order.OrderRepository;
import gift.order.OrderRequest;
import gift.order.OrderResponse;
import gift.order.OrderService;
import gift.product.model.Product;
import gift.wish.WishRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private OauthTokenRepository oauthTokenRepository;
    @Mock
    private WishRepository wishRepository;
    @Mock
    private KakaoMessageClient kakaoMessageClient;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(memberRepository, orderRepository, optionRepository,
            oauthTokenRepository,
            wishRepository, kakaoMessageClient);
    }

    @Test
    void createOrder() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "image.jpg", category);
        Option option = new Option(1L, "option", 3, product);
        Member member = new Member(1L, "test@test.com", "test", "test", 10000);
        given(memberRepository.findById(any())).willReturn(
            Optional.of(member));
        given(optionRepository.findById(any())).willReturn(
            Optional.of(option));
        given(wishRepository.existsByMemberIdAndProductId(any(), any())).willReturn(false);
        given(oauthTokenRepository.findByMemberId(any())).willReturn(
            Optional.of(
                new OauthToken("kakao", "email", "acessToken", 50000, "refreshToken", null)));

        OrderResponse orderResponse = orderService.createOrder(
            new OrderRequest(1L, 2, "hello", true, 500),
            new LoginMemberDto(1L, "test", "test@test.com", "test"));
        then(orderRepository).should().save(any());
        then(kakaoMessageClient).should().sendOrderMessage(any(), any());

        assertAll(
            () -> assertThat(orderResponse.totalPrice()).isEqualTo(2000),
            () -> assertThat(orderResponse.discountedPrice()).isEqualTo(500),
            () -> assertThat(orderResponse.accumulatedPoint()).isEqualTo(150)
        );
    }
}
