package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.auth.dto.LoginReqDto;
import gift.auth.dto.RegisterResDto;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

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

    // 조회할 카테고리 ID 목록
    private static final List<Long> categoryIds = new ArrayList<>();

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
    private static final List<ProductResDto> productList = new ArrayList<>();

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

        // 전체 카테고리 ID 조회
        var categoryRequest = TestUtils.createRequestEntity(categoryUrl, null, HttpMethod.GET, accessToken);
        var categoryResponse = restTemplate.exchange(categoryRequest, new ParameterizedTypeReference<List<CategoryResDto>>() {});
        categoryIds.addAll(categoryResponse.getBody().stream().map(CategoryResDto::id).toList());


        // 상품 초기화
        var productUrl = baseUrl + "/api/products";
        products.forEach(productReqDto -> {
            var productRequest = TestUtils.createRequestEntity(productUrl, productReqDto, HttpMethod.POST, accessToken);
            restTemplate.exchange(productRequest, String.class);
        });

        // 테스트에 이용하기 위해 저장된 상품 불러오기
        getProductResDtos(productUrl);
    }

    private String registration(String mail) {
        var url = baseUrl + "/api/members/register";
        var reqBody = new LoginReqDto(mail, "1234");
        var requestEntity = new RequestEntity<>(reqBody, HttpMethod.POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, RegisterResDto.class);

        assert actual.getBody() != null;
        return actual.getBody().token();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        accessToken = registration("wishListController@test.com");

        memberId = jwtTokenProvider.getMemberId(accessToken);

        // 위시 리스트 초기화
        var wishListUrl = baseUrl + "/api/wishes";
        List.of(
                WishListFixture.createWishListReqDto(productList.get(0).id()),
                WishListFixture.createWishListReqDto(productList.get(1).id()),
                WishListFixture.createWishListReqDto(productList.get(2).id())
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

    private void getProductResDtos(String productUrl) {
        categoryIds.forEach(categoryId -> {
            var uri = UriComponentsBuilder.fromUriString(productUrl)
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .queryParam("sort", "id,desc")  // id 역순 정렬
                    .queryParam("categoryId", categoryId)
                    .build().toString();

            var request = TestUtils.createRequestEntity(uri, null, HttpMethod.GET, accessToken);
            var responseType = new ParameterizedTypeReference<RestPage<ProductResDto>>() {};
            var actual = restTemplate.exchange(request, responseType);
            productList.addAll(actual.getBody().getContent());
        });
    }

    private List<WishListResDto> getWishList() {
        var url = baseUrl + "/api/wishes";
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
            assertThat(w.product()).isNotNull();
        });

        // 위시 리스트에 담긴 상품에서 ID를 추출한 뒤 상품 목록의 ID 비교: 마지막 상품은 담지 않았음
        assertThat(wishList).extracting((w) -> w.product().id())
                .containsExactly(productList.get(0).id(), productList.get(1).id(), productList.get(2).id());
    }

    @Test
    @DisplayName("위시 리스트 추가")
    void 위시_리스트_추가() {
        // given
        long productId = productList.get(3).id();

        var url = baseUrl + "/api/wishes";
        var reqBody = WishListFixture.createWishListReqDto(productId);
        var request = TestUtils.createRequestEntity(url, reqBody, HttpMethod.POST, accessToken);

        // when
        var actual = restTemplate.exchange(request, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody()).isEqualTo("상품을 장바구니에 담았습니다.");
        assertThat(actual.getHeaders().getLocation()).isEqualTo(URI.create("/api/wishes"));

        // 위시 리스트 추가 후 조회
        var wishList = getWishList();

        assertThat(wishList).isNotNull();
        assertThat(wishList).hasSize(4);
        assertThat(wishList).extracting(WishListResDto::product)
                .anyMatch(p -> p.id().equals(productId));
    }

    @Test
    @DisplayName("위시 리스트 추가 실패 - 존재하지 않는 상품 ID")
    void 위시_리스트_추가_실패_존재하지_않는_상품() {
        // given
        var url = baseUrl + "/api/wishes";
        var reqBody = WishListFixture.createWishListReqDto(-1L);    // 존재하지 않는 상품 ID
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

    @Test
    @DisplayName("위시 리스트 추가 실패 - 이미 추가된 상품")
    void 위시_리스트_추가_실패_이미_추가된_상품() {
        //given
        var productId = productList.getFirst().id();
        var url = baseUrl + "/api/wishes";
        var reqBody = WishListFixture.createWishListReqDto(productId);
        var request = TestUtils.createRequestEntity(url, reqBody, HttpMethod.POST, accessToken);

        //when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(WishListErrorCode.WISH_LIST_ALREADY_EXISTS.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo(WishListErrorCode.WISH_LIST_ALREADY_EXISTS.getMessage());
        assertThat(errorResponse.getInvalidParams()).isNull();
    }

    @Test
    @DisplayName("위시 리스트 삭제")
    void 위시_리스트_삭제() {
        // given
        // 자신의 위시 리스트 조회
        var wishList = getWishList();

        // when
        wishList.forEach(w -> {
            var urlDelete = baseUrl + "/api/wishes/" + w.id();
            var requestDelete = TestUtils.createRequestEntity(urlDelete, null, HttpMethod.DELETE, accessToken);
            restTemplate.exchange(requestDelete, String.class);
        });

        // then
        // 삭제 후 위시 리스트 조회
        var wishListAfterDelete = getWishList();

        assertThat(wishListAfterDelete).isEmpty();
    }

    @Test
    @DisplayName("위시 리스트 삭제 실패")
    void 위시_리스트_삭제_실패() {
        // given
        var wishListId = -1L;   // 존재하지 않는 위시 리스트 ID
        var url = baseUrl + "/api/wishes/" + wishListId;
        var request = TestUtils.createRequestEntity(url, null, HttpMethod.DELETE, accessToken);

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
}
