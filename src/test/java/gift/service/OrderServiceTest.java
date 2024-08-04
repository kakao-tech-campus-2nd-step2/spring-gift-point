package gift.service;

import gift.domain.*;
import gift.dto.request.OrderRequestDto;
import gift.dto.response.OrderResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.repository.member.MemberRepository;
import gift.repository.option.OptionRepository;
import gift.repository.order.OrderRepository;
import gift.repository.wish.WishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static gift.exception.exceptionMessage.ExceptionMessage.MEMBER_NOT_FOUND;
import static gift.exception.exceptionMessage.ExceptionMessage.OPTION_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KakaoService kakaoService;

    @Test
    @DisplayName("주문 시 Option NOT FOUND EXCEPTION TEST")
    void 주문_OPTION_NOT_FOUND_테스트(){
        //given
        Long invalidOptionId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(invalidOptionId, 100, "안되지롱", 0);
        AuthToken authToken = new AuthToken("안되지롱", "안되지롱");
        given(optionRepository.findOptionByIdForUpdate(invalidOptionId)).willReturn(Optional.empty());

        //expected
        assertThatThrownBy(() -> orderService.addOrder(orderRequestDto, authToken))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(OPTION_NOT_FOUND);
    }

    @Test
    @DisplayName("주문 시 Member NOT FOUND EXCEPTION TEST")
    void 주문_Member_NOT_FOUND_테스트(){
        //given
        Long validOptionId = 1L;
        OrderRequestDto orderRequestDto = new OrderRequestDto(validOptionId, 100, "테스트", 0);
        Option option = new Option("테스트", 100);
        AuthToken authToken = new AuthToken("1234", "123@kakao.com");
        given(optionRepository.findOptionByIdForUpdate(validOptionId)).willReturn(Optional.of(option));
        given(memberRepository.findMemberByEmailForUpdate(authToken.getEmail())).willReturn(Optional.empty());

        //expected
        assertThatThrownBy(() -> orderService.addOrder(orderRequestDto, authToken))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("주문 시 상품이 WISH LIST에 있을 때 삭제 TEST")
    void 주문_WISH_LIST_삭제_테스트() throws Exception{
        //given
        Long validOptionId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(validOptionId, 100, "테스트", 0);

        Option option = new Option("테스트", 100);

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(10000)
                .imageUrl("abc.png")
                .category(null)
                .build();
        option.addProduct(product);

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        AuthToken authToken = new AuthToken("1234", "123@kakao.com");

        Member member = new Member.Builder()
                .email("123@kakao.com")
                .password("123@kakao.com")
                .kakaoId("123")
                .build();

        Order order = new Order.Builder()
                .option(option)
                .member(member)
                .quantity(orderRequestDto.quantity())
                .message(orderRequestDto.message())
                .build();

        Field createdAtField = Order.class.getDeclaredField("orderDateTime");
        createdAtField.setAccessible(true);
        createdAtField.set(order, LocalDateTime.now());

        Wish wish = new Wish();

        given(optionRepository.findOptionByIdForUpdate(validOptionId)).willReturn(Optional.of(option));
        given(memberRepository.findMemberByEmailForUpdate(authToken.getEmail())).willReturn(Optional.of(member));
        given(wishRepository.findWishByProductIdAndMemberEmail(anyLong(), anyString())).willReturn(Optional.of(wish));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        //when
        OrderResponseDto orderResponseDto = orderService.addOrder(orderRequestDto, authToken);

        //then
        assertAll(
                () -> assertThat(orderResponseDto.quantity()).isEqualTo(orderRequestDto.quantity()),
                () -> assertThat(orderResponseDto.message()).isEqualTo(orderRequestDto.message()),
                () -> verify(wishRepository,times(1)).delete(any(Wish.class)),
                () -> verify(kakaoService,times(0)).sendKakaoMessage(any(String.class), any(OrderResponseDto.class))
        );
    }

    @Test
    @DisplayName("주문 시 상품이 WISH LIST에 없을 때 삭제 X TEST")
    void 주문_WISH_LIST_삭제_X_테스트() throws Exception{
        //given
        Long validOptionId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(validOptionId, 100, "테스트", 0);

        Option option = new Option("테스트", 100);

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(10000)
                .imageUrl("abc.png")
                .category(null)
                .build();

        option.addProduct(product);

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        AuthToken authToken = new AuthToken.Builder()
                .token("테스트 토큰")
                .tokenTime(3000)
                .email("123@kakao.com")
                .accessToken("a1b2c3")
                .accessTokenTime(300000)
                .refreshToken("a2b3c4d5")
                .refreshTokenTime(300000)
                .build();

        Member member = new Member.Builder()
                .email("123@kakao.com")
                .password("123@kakao.com")
                .kakaoId("123")
                .build();

        Order order = new Order.Builder()
                .option(option)
                .member(member)
                .quantity(orderRequestDto.quantity())
                .message(orderRequestDto.message())
                .build();

        Field createdAtField = Order.class.getDeclaredField("orderDateTime");
        createdAtField.setAccessible(true);
        createdAtField.set(order, LocalDateTime.now());

        given(optionRepository.findOptionByIdForUpdate(validOptionId)).willReturn(Optional.of(option));
        given(memberRepository.findMemberByEmailForUpdate(authToken.getEmail())).willReturn(Optional.of(member));
        given(wishRepository.findWishByProductIdAndMemberEmail(anyLong(), anyString())).willReturn(Optional.empty());
        given(orderRepository.save(any(Order.class))).willReturn(order);

        //when
        OrderResponseDto orderResponseDto = orderService.addOrder(orderRequestDto, authToken);

        //then
        assertAll(
                () -> assertThat(orderResponseDto.quantity()).isEqualTo(orderRequestDto.quantity()),
                () -> assertThat(orderResponseDto.message()).isEqualTo(orderRequestDto.message()),
                () -> verify(wishRepository,times(0)).delete(any(Wish.class)),
                () -> verify(kakaoService,times(1)).sendKakaoMessage(any(String.class), any(OrderResponseDto.class))
        );
    }

    @Test
    @DisplayName("주문 시 상품이 포인트 사용 TEST")
    void 주문_포인트_사용_테스트() throws Exception{
        //given
        Long validOptionId = 1L;

        OrderRequestDto orderRequestDto = new OrderRequestDto(validOptionId, 10, "테스트", 100);

        Option option = new Option("테스트", 100);

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(100)
                .imageUrl("abc.png")
                .category(null)
                .build();

        option.addProduct(product);

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        AuthToken authToken = new AuthToken.Builder()
                .token("테스트 토큰")
                .tokenTime(3000)
                .email("123@kakao.com")
                .accessToken("a1b2c3")
                .accessTokenTime(300000)
                .refreshToken("a2b3c4d5")
                .refreshTokenTime(300000)
                .build();

        Member member = new Member.Builder()
                .email("123@kakao.com")
                .password("123@kakao.com")
                .kakaoId("123")
                .build();

        Field pointField = Member.class.getDeclaredField("point");
        pointField.setAccessible(true);
        pointField.set(member, 1000);

        Order order = new Order.Builder()
                .option(option)
                .member(member)
                .quantity(orderRequestDto.quantity())
                .message(orderRequestDto.message())
                .build();

        Field createdAtField = Order.class.getDeclaredField("orderDateTime");
        createdAtField.setAccessible(true);
        createdAtField.set(order, LocalDateTime.now());

        given(optionRepository.findOptionByIdForUpdate(validOptionId)).willReturn(Optional.of(option));
        given(memberRepository.findMemberByEmailForUpdate(authToken.getEmail())).willReturn(Optional.of(member));
        given(wishRepository.findWishByProductIdAndMemberEmail(anyLong(), anyString())).willReturn(Optional.empty());
        given(orderRepository.save(any(Order.class))).willReturn(order);

        //when
        OrderResponseDto orderResponseDto = orderService.addOrder(orderRequestDto, authToken);

        //then
        assertAll(
                () -> assertThat(orderResponseDto.quantity()).isEqualTo(orderRequestDto.quantity()),
                () -> assertThat(orderResponseDto.message()).isEqualTo(orderRequestDto.message()),
                () -> assertThat(member.getPoint()).isEqualTo(909),
                () -> verify(wishRepository,times(0)).delete(any(Wish.class)),
                () -> verify(kakaoService,times(1)).sendKakaoMessage(any(String.class), any(OrderResponseDto.class))
        );
    }
}
