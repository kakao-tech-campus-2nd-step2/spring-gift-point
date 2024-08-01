package gift.service;

import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.member.Member;
import gift.domain.member.SocialAccount;
import gift.domain.member.SocialType;
import gift.dto.OrderDto;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.KakaoApiService;
import gift.service.kakao.Oauth2TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Oauth2TokenService oauth2TokenService;

    @Mock
    private KakaoApiService kakaoApiService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 처리하고 나에게 카카오톡 메시지를 전송한다.")
    @Test
    void processOrder() throws Exception {
        //given
        SocialAccount socialAccount = new SocialAccount(SocialType.KAKAO, 123L, "test-access-token", "test-refresh-token");
        Member member = new Member.MemberBuilder().socialAccount(socialAccount).point(5000).build();

        Long originalQuantity = 10L;
        Long orderQuantity = 5L;
        String message = "Test Message";

        OrderDto orderDto = new OrderDto(1L, 2L, orderQuantity, member, message, 3000);

        Option option = new Option();
        option.setProduct(new Product());
        option.setId(1L);
        option.setQuantity(originalQuantity);

        given(oauth2TokenService.isAccessTokenExpired(anyString())).willReturn(false);
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).willReturn(Optional.of(new Wish()));
        willDoNothing().given(wishRepository).delete(any(Wish.class));
        willDoNothing().given(kakaoApiService).sendKakaoMessage(anyString(), anyString());
        given(orderRepository.save(any(Order.class))).willReturn(new Order(option, orderQuantity, message, member));

        //when
        OrderDto result = orderService.processOrder(orderDto);

        //then
        assertThat(result.getQuantity()).isEqualTo(orderQuantity);
        assertThat(result.getMessage()).isEqualTo(message);

        then(oauth2TokenService).should().isAccessTokenExpired(anyString());
        then(optionRepository).should().findById(anyLong());
        then(wishRepository).should().findByMemberAndProduct(any(Member.class), any(Product.class));
        then(wishRepository).should().delete(any(Wish.class));
        then(kakaoApiService).should().sendKakaoMessage(anyString(), anyString());
        then(orderRepository).should().save(any(Order.class));
    }

    @DisplayName("주문을 처리하고 카카오 메시지를 전송하는데, 만료된 Access Token 이면 Refresh Token 을 통해 재발급 받고 진행한다.")
    @Test
    void processOrderWithExpiredAccessToken() throws Exception {
        //given
        SocialAccount socialAccount = new SocialAccount(SocialType.KAKAO, 123L, "test-access-token", "test-refresh-token");
        Member member = new Member.MemberBuilder().socialAccount(socialAccount).point(5000).build();

        Long originalQuantity = 10L;
        Long orderQuantity = 5L;
        String message = "Test Message";

        OrderDto orderDto = new OrderDto(1L, 2L, orderQuantity, member, message, 3000);

        Option option = new Option();
        option.setProduct(new Product());
        option.setId(1L);
        option.setQuantity(originalQuantity);

        given(oauth2TokenService.isAccessTokenExpired(anyString())).willReturn(true);
        willDoNothing().given(oauth2TokenService).refreshAccessToken(any(SocialAccount.class));
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).willReturn(Optional.of(new Wish()));
        willDoNothing().given(wishRepository).delete(any(Wish.class));
        willDoNothing().given(kakaoApiService).sendKakaoMessage(anyString(), anyString());
        given(orderRepository.save(any(Order.class))).willReturn(new Order(option, orderQuantity, message, member));

        //when
        OrderDto result = orderService.processOrder(orderDto);

        //then
        assertThat(result.getQuantity()).isEqualTo(orderQuantity);
        assertThat(result.getMessage()).isEqualTo(message);

        then(oauth2TokenService).should().isAccessTokenExpired(anyString());
        then(oauth2TokenService).should().refreshAccessToken(any(SocialAccount.class));
        then(optionRepository).should().findById(anyLong());
        then(wishRepository).should().findByMemberAndProduct(any(Member.class), any(Product.class));
        then(wishRepository).should().delete(any(Wish.class));
        then(kakaoApiService).should().sendKakaoMessage(anyString(), anyString());
        then(orderRepository).should().save(any(Order.class));
    }

}
