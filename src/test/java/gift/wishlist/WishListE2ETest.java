package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.auth.dto.LoginReqDto;
import gift.auth.token.AuthToken;
import gift.category.CategoryFixture;
import gift.category.dto.CategoryResDto;
import gift.common.exception.ErrorResponse;
import gift.option.OptionFixture;
import gift.option.dto.OptionReqDto;
import gift.product.ProductFixture;
import gift.product.dto.ProductReqDto;
import gift.product.dto.ProductResDto;
import gift.utils.JwtTokenProvider;
import gift.utils.RestPage;
import gift.utils.TestUtils;
import gift.wishlist.dto.WishListResDto;
import gift.wishlist.exception.WishListErrorCode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DisplayName("위시 리스트 API 테스트")
class WishListE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static String baseUrl;
    private static String accessToken;

    private static final List<OptionReqDto> options = List.of(
            OptionFixture.createOptionReqDto("옵션1", 1000),
            OptionFixture.createOptionReqDto("옵션2", 2000),
            OptionFixture.createOptionReqDto("옵션3", 3000)
    );

    private static final List<ProductReqDto> products = List.of(
            ProductFixture.createProductReqDto("상품1", 1000, "keyboard.png", "위시리스트 카테고리1", options),
            ProductFixture.createProductReqDto("상품2", 2000, "mouse.png", "위시리스트 카테고리2", options),
            ProductFixture.createProductReqDto("상품3", 3000, "monitor.png", "위시리스트 카테고리3", options),
            ProductFixture.createProductReqDto("상품4", 4000, "headset.png", "위시리스트 카테고리4", options)
    );
    private static List<ProductResDto> productList = new ArrayList<>();

    private static Long memberId;

    @BeforeAll
    void beforeAll() {
        baseUrl = "http://localhost:" + port;

        // 상품 등록을 위한 임의의 회원 가입
        accessToken = registration("wishListE2E@test.com");

        String categoryUrl = baseUrl + "/api/categories";
        List.of(
                CategoryFixture.createCategoryReqDto("위시리스트 카테고리1", "#FF0000", "category1.png", "카테고리1 입니다."),
                CategoryFixture.createCategoryReqDto("위시리스트 카테고리2", "#00FF00", "category2.png", "카테고리2 입니다."),
                CategoryFixture.createCategoryReqDto("위시리스트 카테고리3", "#0000FF", "category3.png", "카테고리3 입니다."),
                CategoryFixture.createCategoryReqDto("위시리스트 카테고리4", "#FFFF00", "category4.png", "카테고리4 입니다.")
        ).forEach(categoryReqDto -> {
            var categoryRequest = TestUtils.createRequestEntity(categoryUrl, categoryReqDto, HttpMethod.POST, accessToken);
            restTemplate.exchange(categoryRequest, String.class);
        });

        // 상품 초기화
        var productUrl = baseUrl + "/api/products";
        products.forEach(productReqDto -> {
            var productRequest = TestUtils.createRequestEntity(productUrl, productReqDto, HttpMethod.POST, accessToken);
            restTemplate.exchange(productRequest, String.class);
        });

        // 테스트에 이용하기 위해 저장된 상품 불러오기
        productList = getProductResDtos(productUrl);
    }

    private String registration(String mail) {
        var url = baseUrl + "/api/members/register";
        var reqBody = new LoginReqDto(mail, "1234");
        var requestEntity = new RequestEntity<>(reqBody, HttpMethod.POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, AuthToken.class);

        assert actual.getBody() != null;
        return actual.getBody().accessToken();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        accessToken = registration("wishListController@test.com");

        memberId = jwtTokenProvider.getMemberId(accessToken);

        // 위시 리스트 초기화
        var wishListUrl = baseUrl + "/api/wish-list";
        List.of(
                WishListFixture.createWishListReqDto(productList.get(0).id(), 3),
                WishListFixture.createWishListReqDto(productList.get(1).id(), 7),
                WishListFixture.createWishListReqDto(productList.get(2).id(), 5)
        ).forEach(wishListReqDto -> {
            var wishListRequest = TestUtils.createRequestEntity(wishListUrl, wishListReqDto, HttpMethod.POST, accessToken);
            restTemplate.exchange(wishListRequest, String.class);
        });
    }

    @AfterAll
    void afterAll() {
        // 카테고리 삭제
        var categoryUrl = baseUrl + "/api/categories";
        var categoryRequest = TestUtils.createRequestEntity(categoryUrl, null, HttpMethod.GET, accessToken);
        var categoryResponse = restTemplate.exchange(categoryRequest, new ParameterizedTypeReference<List<CategoryResDto>>() {});
        categoryResponse.getBody().forEach(category -> {
            var deleteRequest = TestUtils.createRequestEntity(categoryUrl + "/" + category.id(), null, HttpMethod.DELETE, accessToken);
            restTemplate.exchange(deleteRequest, String.class);
        });
    }

    private List<ProductResDto> getProductResDtos(String productUrl) {
        var productRequest = TestUtils.createRequestEntity(productUrl, null, HttpMethod.GET, accessToken);
        var responseType = new ParameterizedTypeReference<RestPage<ProductResDto>>() {};
        var actualProduct = restTemplate.exchange(productRequest, responseType);
        return actualProduct.getBody().getContent();
    }

    private List<WishListResDto> getWishList() {
        var url = baseUrl + "/api/wish-list";
        var request = TestUtils.createRequestEntity(url, null, HttpMethod.GET, accessToken);
        var responseType = new ParameterizedTypeReference<RestPage<WishListResDto>>() {};
        var actual = restTemplate.exchange(request, responseType);
        return actual.getBody().getContent();
    }

    @AfterEach
    void tearDown() {
        // 회원 삭제
        var url = baseUrl + "/api/members/" + memberId;
        var request = TestUtils.createRequestEntity(url, null, HttpMethod.DELETE, accessToken);
        restTemplate.exchange(request, String.class);
    }

    @Test
    @DisplayName("위시 리스트 조회")
    void 위시_리스트_조회() {
        // when
        List<WishListResDto> wishList = getWishList();

        // then
        assertThat(wishList).isNotNull();

        assertThat(wishList).hasSize(3);
        wishList.forEach(w -> {
            assertThat(w.id()).isNotNull();
            assertThat(w.productId()).isNotNull();
            assertThat(w.quantity()).isNotNull();
        });

        // 위시 리스트에 담긴 상품 ID와 상품 목록의 ID 비교: 마지막 상품은 담지 않았음
        assertThat(wishList).extracting(WishListResDto::productId)
                .containsExactly(productList.get(0).id(), productList.get(1).id(), productList.get(2).id());

        // 위시 리스트에 담긴 상품 수량 비교
        assertThat(wishList).extracting(WishListResDto::quantity)
                .containsExactly(3, 7, 5);
    }

    @ParameterizedTest(name = "위시 리스트 추가 테스트 - 상품 ID: {0}, 수량: {1}")
    @CsvSource({    // 상품 ID, 수량, 기대 상품 수, 기대 총 수량
            "3, 3, 4, 3",   // productList.get(3): 새로운 상품 추가
            "1, 4, 3, 11"   // productList.get(1): 기존 상품 수량 증가
    })
    @DisplayName("위시 리스트 추가")
    void 위시_리스트_추가(int productIndex, int quantity, int expectedTotalItems, int expectedTotalQuantity) {
        // given
        long productId = productList.get(productIndex).id();

        var url = baseUrl + "/api/wish-list";
        var reqBody = WishListFixture.createWishListReqDto(productId, quantity);
        var request = TestUtils.createRequestEntity(url, reqBody, HttpMethod.POST, accessToken);

        // when
        var actual = restTemplate.exchange(request, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody()).isEqualTo("상품을 장바구니에 담았습니다.");
        assertThat(actual.getHeaders().getLocation()).isEqualTo(URI.create("/api/wish-list"));

        // 위시 리스트 추가 후 조회
        var wishList = getWishList();

        assertThat(wishList).hasSize(expectedTotalItems);
        assertThat(wishList).extracting(WishListResDto::quantity)
                .contains(expectedTotalQuantity);
    }

    @Test
    @DisplayName("위시 리스트 추가 실패")
    void 위시_리스트_추가_실패() {
        // given
        var url = baseUrl + "/api/wish-list";
        var reqBody = WishListFixture.createWishListReqDto(-1L, 3);    // 존재하지 않는 상품 ID
        var request = TestUtils.createRequestEntity(url, reqBody, HttpMethod.POST, accessToken);

        // when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(WishListErrorCode.WISH_LIST_NOT_FOUND.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo(WishListErrorCode.WISH_LIST_NOT_FOUND.getMessage());
        assertThat(errorResponse.getInvalidParams()).isNull();
    }

    @ParameterizedTest(name = "위시 리스트 수정 테스트 - 수정 수량: {0}")
    @MethodSource("provideWishListUpdateScenarios")
    @DisplayName("위시 리스트 수정")
    void 위시_리스트_수정(int updatedQuantity, String expectedMessage,
            BiPredicate<List<WishListResDto>, Long> resultPredicate) {
        // given
        // 자신의 위시 리스트 조회
        var wishList = getWishList();

        // 수정할 마지막 요소 가져오기
        assert wishList != null;
        var lastWishList = wishList.getLast();

        // 수정할 내용 준비
        var urlUpdate = baseUrl + "/api/wish-list/" + lastWishList.id();
        var reqBodyUpdate = WishListFixture.createWishListReqDto(lastWishList.productId(), updatedQuantity);
        var requestUpdate = TestUtils.createRequestEntity(urlUpdate, reqBodyUpdate, HttpMethod.PUT, accessToken);

        // when
        var actual = restTemplate.exchange(requestUpdate, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody()).isEqualTo(expectedMessage);

        // 수정된 위시 리스트 조회
        var updatedWishList = getWishList();

        assertThat(resultPredicate.test(updatedWishList, lastWishList.id())).isTrue();
    }

    private static Stream<Arguments> provideWishListUpdateScenarios() {
        return Stream.of(
                Arguments.of(7, "담긴 상품의 수량을 변경했습니다.",
                        (BiPredicate<List<WishListResDto>, Long>) (wishList, wishListId) ->
                                wishList.stream().anyMatch(w -> w.id().equals(wishListId) && w.quantity() == 7)),

                Arguments.of(3, "담긴 상품의 수량을 변경했습니다.",
                        (BiPredicate<List<WishListResDto>, Long>) (wishList, wishListId) ->
                                wishList.stream().anyMatch(w -> w.id().equals(wishListId) && w.quantity() == 3)),

                // 수량 0: 삭제
                Arguments.of(0, "담긴 상품의 수량을 변경했습니다.",
                        (BiPredicate<List<WishListResDto>, Long>) (wishList, wishListId) ->
                                wishList.stream().noneMatch(w -> w.id().equals(wishListId)))
        );
    }

    @Test
    @DisplayName("위시 리스트 삭제")
    void 위시_리스트_삭제() {
        // given
        // 자신의 위시 리스트 조회
        var wishList = getWishList();

        // when
        assert wishList != null;
        wishList.forEach(w -> {
            var urlDelete = baseUrl + "/api/wish-list/" + w.id();
            var requestDelete = TestUtils.createRequestEntity(urlDelete, null, HttpMethod.DELETE, accessToken);
            restTemplate.exchange(requestDelete, String.class);
        });

        // then
        // 삭제 후 위시 리스트 조회
        var wishListAfterDelete = getWishList();

        assertThat(wishListAfterDelete).isEmpty();
    }

    @ParameterizedTest(name = "위시 리스트 {0} 실패")
    @MethodSource("provideWishListModifyFailureScenarios")
    @DisplayName("위시 리스트 수정/삭제 실패")
    void 위시_리스트_수정_삭제_실패(String operation, HttpMethod httpMethod, Object reqBody) {
        // given
        var wishListId = -1L;   // 존재하지 않는 위시 리스트 ID
        var url = baseUrl + "/api/wish-list/" + wishListId;
        var request = TestUtils.createRequestEntity(url, reqBody, httpMethod, accessToken);

        // when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(WishListErrorCode.WISH_LIST_NOT_FOUND.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo(WishListErrorCode.WISH_LIST_NOT_FOUND.getMessage());
        assertThat(errorResponse.getInvalidParams()).isNull();
    }

    private static Stream<Arguments> provideWishListModifyFailureScenarios() {
        return Stream.of(
                Arguments.of("수정", HttpMethod.PUT, WishListFixture.createWishListReqDto(1L, 3)),
                Arguments.of("삭제", HttpMethod.DELETE, null)
        );
    }
}
