package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.order.OrderDto;
import gift.product.exception.LoginFailedException;
import gift.product.model.Category;
import gift.product.model.KakaoToken;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.model.Product;
import gift.product.repository.AuthRepository;
import gift.product.repository.KakaoTokenRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.repository.WishRepository;
import gift.product.service.OrderService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    public static final String TEST_OAUTH_ACCESS_TOKEN = "test_oauth_access_token";
    public static final String TEST_OAUTH_REFRESH_TOKEN = "test_oauth_refresh_token";
    MockWebServer mockWebServer;
    ObjectMapper objectMapper;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OptionRepository optionRepository;

    @Mock
    WishRepository wishRepository;

    @Mock
    AuthRepository authRepository;

    @Mock
    KakaoTokenRepository kakaoTokenRepository;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void ObjectMapper_셋팅() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void 가짜_API_서버_구동() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void 가짜_API_서버_종료() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void 주문_전체_조회() {
        //given
        int PAGE = 1;
        int SIZE = 4;
        String SORT = "orderDateTime";
        String DIRECTION = "desc";
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.Direction.fromString(DIRECTION), SORT);
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1000, product);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(option.getId(), loginMemberIdDto.id(), 1, "test_message"));

        given(orderRepository.findAllByMemberId(pageable,
            loginMemberIdDto.id())).willReturn(new PageImpl<>(orders));
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));

        //when
        orderService.getOrderAll(pageable, loginMemberIdDto);

        //then
        then(orderRepository).should().findAllByMemberId(pageable, loginMemberIdDto.id());
    }

    @Test
    void 주문_조회() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L, 1L, loginMemberIdDto.id(), 1, "test_message");
        given(orderRepository.findByIdAndMemberId(order.getId(), loginMemberIdDto.id())).willReturn(
            Optional.of(order));

        //when
        orderService.getOrder(order.getId(), loginMemberIdDto);

        //then
        then(orderRepository).should().findByIdAndMemberId(order.getId(), loginMemberIdDto.id());
    }

    @Test
    void 주문() {
        //given
        OrderDto orderDto = new OrderDto(1L, 3, "test_message");
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L,
            orderDto.optionId(),
            loginMemberIdDto.id(),
            2,
            orderDto.message());
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);
        KakaoToken kakaoToken = new KakaoToken(1L,
            loginMemberIdDto.id(),
            TEST_OAUTH_ACCESS_TOKEN,
            TEST_OAUTH_REFRESH_TOKEN);

        String resultCode = "{\"result_code\":0}";
        mockWebServer.enqueue(new MockResponse().setBody(resultCode));

        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(any(), any())).willReturn(true);
        given(orderRepository.save(any())).willReturn(order);
        given(kakaoTokenRepository.findByMemberId(any())).willReturn(Optional.of(kakaoToken));

        //when
        String mockUrl = mockWebServer.url("/v2/api/talk/memo/default/send").toString();
        Order resultOrder = orderService.doOrder(orderDto, loginMemberIdDto, mockUrl);

        //then
        assertThat(resultOrder.getId()).isNotNull();
    }

    @Test
    void 주문_삭제() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L, 1L, loginMemberIdDto.id(), 1, "test_message");
        given(orderRepository.existsById(order.getId())).willReturn(true);

        //when
        orderService.deleteOrder(order.getId(), loginMemberIdDto);

        //then
        then(orderRepository).should().deleteByIdAndMemberId(order.getId(), loginMemberIdDto.id());
    }

    @Test
    void 실패_옵션_수량보다_더_많이_차감() {
        //given
        Category category = new Category("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1, product);
        OrderDto orderDto = new OrderDto(option.getId(), 999, "test_message");
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);

        //when, then
        assertThatThrownBy(
            () -> orderService.doOrder(orderDto, loginMemberIdDto, "test_url")).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    void 실패_존재하지_않는_주문_내역_조회() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        given(orderRepository.findByIdAndMemberId(1L,
            loginMemberIdDto.id())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.getOrder(1L, loginMemberIdDto)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_주문_시_카카오톡_메시지_API_에러() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        OrderDto orderDto = new OrderDto(1L, 2, "test_message");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);
        Order order = new Order(1L,
            option.getId(),
            loginMemberIdDto.id(),
            orderDto.quantity(),
            orderDto.message());
        KakaoToken kakaoToken = new KakaoToken(1L,
            TEST_OAUTH_ACCESS_TOKEN,
            TEST_OAUTH_REFRESH_TOKEN);

        mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        String mockUrl = mockWebServer.url("/v2/api/talk/memo/default/send").toString();

        given(optionRepository.findById(orderDto.optionId())).willReturn(Optional.of(option));
        given(authRepository.existsById(loginMemberIdDto.id())).willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(product.getId(),
            loginMemberIdDto.id())).willReturn(false);
        given(orderRepository.save(any())).willReturn(order);
        given(kakaoTokenRepository.findByMemberId(loginMemberIdDto.id())).willReturn(Optional.of(
            kakaoToken));

        //when, then
        assertThatThrownBy(() -> orderService.doOrder(orderDto,
            loginMemberIdDto,
            mockUrl)).isInstanceOf(
            LoginFailedException.class).hasMessage("카카오톡 메시지 API 관련 에러가 발생하였습니다. 다시 시도해주세요.");
    }

    @Test
    void 실패_존재하지_않는_옵션에_대한_주문() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        OrderDto orderDto = new OrderDto(1L, 2, "test_message");
        given(optionRepository.findById(orderDto.optionId())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.doOrder(orderDto,
            loginMemberIdDto,
            "test_url")).isInstanceOf(
            NoSuchElementException.class).hasMessage("해당 ID의 옵션이 존재하지 않습니다.");
    }

    @Test
    void 실패_존재하지_않는_회원_정보로_주문() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        OrderDto orderDto = new OrderDto(1L, 2, "test_message");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);

        given(optionRepository.findById(orderDto.optionId())).willReturn(Optional.of(option));
        given(authRepository.existsById(loginMemberIdDto.id())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> orderService.doOrder(orderDto,
            loginMemberIdDto,
            "test_url")).isInstanceOf(
            NoSuchElementException.class).hasMessage("해당 ID의 회원이 존재하지 않습니다.");
    }

    @Test
    void 실패_카카오_로그인을_하지_않은_회원이_주문_요청() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        OrderDto orderDto = new OrderDto(1L, 2, "test_message");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);

        given(optionRepository.findById(orderDto.optionId())).willReturn(Optional.of(option));
        given(authRepository.existsById(loginMemberIdDto.id())).willReturn(true);
        given(kakaoTokenRepository.findByMemberId(loginMemberIdDto.id())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.doOrder(orderDto,
            loginMemberIdDto,
            "test_url")).isInstanceOf(
            NoSuchElementException.class).hasMessage("카카오 계정 로그인을 수행한 후 다시 시도해주세요.");
    }

    @Test
    void 실패_주문_시_카카오톡_메시지_API_응답_결과_코드가_실패인_경우() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        OrderDto orderDto = new OrderDto(1L, 2, "test_message");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);
        Order order = new Order(1L,
            option.getId(),
            loginMemberIdDto.id(),
            orderDto.quantity(),
            orderDto.message());
        KakaoToken kakaoToken = new KakaoToken(1L,
            TEST_OAUTH_ACCESS_TOKEN,
            TEST_OAUTH_REFRESH_TOKEN);

        String resultCode = "{\"result_code\":1}";
        mockWebServer.enqueue(new MockResponse().setBody(resultCode));
        String mockUrl = mockWebServer.url("/v2/api/talk/memo/default/send").toString();

        given(optionRepository.findById(orderDto.optionId())).willReturn(Optional.of(option));
        given(authRepository.existsById(loginMemberIdDto.id())).willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(product.getId(),
            loginMemberIdDto.id())).willReturn(false);
        given(orderRepository.save(any())).willReturn(order);
        given(kakaoTokenRepository.findByMemberId(loginMemberIdDto.id())).willReturn(Optional.of(
            kakaoToken));

        //when, then
        assertThatThrownBy(() -> orderService.doOrder(orderDto,
            loginMemberIdDto,
            mockUrl)).isInstanceOf(
            LoginFailedException.class).hasMessage("카카오톡 메시지 API 관련 에러가 발생하였습니다. 다시 시도해주세요.");
    }

    @Test
    void 실패_존재하지_않는_주문_내역_삭제() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        given(orderRepository.existsById(1L)).willReturn(false);

        //when, then
        assertThatThrownBy(() -> orderService.deleteOrder(1L, loginMemberIdDto)).isInstanceOf(
            NoSuchElementException.class);
    }
}
