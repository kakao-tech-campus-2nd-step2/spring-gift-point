package gift.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

import gift.auth.dto.LoginReqDto;
import gift.auth.token.AuthToken;
import gift.category.dto.CategoryResDto;
import gift.category.exception.CategoryErrorCode;
import gift.common.exception.CommonErrorCode;
import gift.common.exception.ErrorResponse;
import gift.common.exception.ValidationError;
import gift.utils.TestUtils;
import java.net.URI;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DisplayName("카테고리 API 테스트")
class CategoryE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private String accessToken;

    @BeforeAll
    void setUp() {
        baseUrl = "http://localhost:" + port;

        var url = baseUrl + "/api/members/register";
        var reqBody = new LoginReqDto("categoryE2E@test.com", "1234");
        var requestEntity = new RequestEntity<>(reqBody, HttpMethod.POST, URI.create(url));
        var actual = restTemplate.exchange(requestEntity, AuthToken.class);

        accessToken = actual.getBody().accessToken();
    }

    @BeforeEach
    void init() {
        // 카테고리 추가
        var categoryUrl = baseUrl + "/api/categories";
        List.of(
                CategoryFixture.createCategoryReqDto("카테고리1", "#f3f3f3", "http://localhost:8080/image1.jpg", "카테고리1입니다."),
                CategoryFixture.createCategoryReqDto("카테고리2", "#223432", "http://localhost:8080/image2.jpg", "카테고리2입니다."),
                CategoryFixture.createCategoryReqDto("카테고리3", "#5656ff", "http://localhost:8080/image3.jpg", "카테고리3입니다.")
        ).forEach(categoryReqDto -> {
            var request = TestUtils.createRequestEntity(categoryUrl, categoryReqDto, HttpMethod.POST, accessToken);
            restTemplate.exchange(request, String.class);
        });
    }

    @AfterEach
    void tearDown() {
        // 추가 테스트 이후 전체 카테고리를 조회하면 4개가 나오는 문제 발생.
        // 테스트 코드간 데이터 공유를 막기 위해 추가한 코드
        var categories = getCategoryList();
        categories.forEach(category -> {
            var url = baseUrl + "/api/categories/" + category.id();
            var request = TestUtils.createRequestEntity(url, null, HttpMethod.DELETE, accessToken);
            restTemplate.exchange(request, String.class);
        });
    }

    private List<CategoryResDto> getCategoryList() {
        var url = baseUrl + "/api/categories";
        var request = TestUtils.createRequestEntity(url, null, HttpMethod.GET, accessToken);
        var responseType = new ParameterizedTypeReference<List<CategoryResDto>>() {};
        var actual = restTemplate.exchange(request, responseType);
        return actual.getBody();
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void getCategories() {
        //given
        String url = baseUrl + "/api/categories";
        RequestEntity<Object> request = TestUtils.createRequestEntity(url, null, HttpMethod.GET, accessToken);

        //when
        var responseType = new ParameterizedTypeReference<List<CategoryResDto>>() {};
        var actual = restTemplate.exchange(request, responseType);
        var categories = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(categories).isNotNull();
        assertThat(categories).hasSize(3);
        assertThatList(categories).map(CategoryResDto::name).containsExactly("카테고리1", "카테고리2", "카테고리3");
        assertThatList(categories).map(CategoryResDto::color).containsExactly("#f3f3f3", "#223432", "#5656ff");
        assertThatList(categories).map(CategoryResDto::imageUrl).containsExactly("http://localhost:8080/image1.jpg", "http://localhost:8080/image2.jpg", "http://localhost:8080/image3.jpg");
        assertThatList(categories).map(CategoryResDto::description).containsExactly("카테고리1입니다.", "카테고리2입니다.", "카테고리3입니다.");

        categories.forEach(category -> assertThat(category).isInstanceOf(CategoryResDto.class));
    }

    @Test
    @DisplayName("카테고리 추가")
    void addCategory() {
        //given
        var reqbody = CategoryFixture.createCategory("카테고리4", "#f3f3f3", "http://localhost:8080/image4.jpg", "카테고리4입니다.");
        var request = TestUtils.createRequestEntity("/api/categories", reqbody, HttpMethod.POST, accessToken);

        //when
        var actual = restTemplate.exchange(request, String.class);
        var category = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getHeaders().getLocation()).isEqualTo(URI.create("/api/categories"));
        assertThat(category).isEqualTo("카테고리를 추가했습니다.");

        var categories = getCategoryList();
        assertThatList(categories).hasSize(4);

        var newCategory = categories.getLast();
        assertThat(newCategory.name()).isEqualTo("카테고리4");
        assertThat(newCategory.color()).isEqualTo("#f3f3f3");
        assertThat(newCategory.imageUrl()).isEqualTo("http://localhost:8080/image4.jpg");
        assertThat(newCategory.description()).isEqualTo("카테고리4입니다.");
    }

    @Test
    @DisplayName("카테고리 추가 실패")
    void failAddCategory() {
        //given
        var reqbody = CategoryFixture.createCategory("", "", "", "생성 실패할 카테고리");
        var request = TestUtils.createRequestEntity("/api/categories", reqbody, HttpMethod.POST, accessToken);

        //when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(CommonErrorCode.INVALID_INPUT_VALUE.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo(CommonErrorCode.INVALID_INPUT_VALUE.getMessage());

        List<ValidationError> invalidParams = errorResponse.getInvalidParams();
        assertThat(invalidParams).hasSize(3);
        assertThat(invalidParams.parallelStream().map(ValidationError::message)).containsExactlyInAnyOrder(
                "카테고리 이름은 필수입니다.",
                "카테고리 색상은 필수입니다.",
                "카테고리 이미지 URL은 필수입니다."
        );
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() {
        //given: 마지막 카테고리 수정
        var categories = getCategoryList();
        var lastCategory = categories.getLast();

        var reqbody = CategoryFixture.createCategory("수정된 카테고리", "#fefefe", "http://localhost:8080/updated.jpg", "수정된 카테고리입니다.");
        var request = TestUtils.createRequestEntity("/api/categories/" + lastCategory.id(), reqbody, HttpMethod.PUT, accessToken);

        //when
        var actual = restTemplate.exchange(request, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo("카테고리 정보를 수정했습니다.");

        var updatedCategory = getCategoryList().getLast();
        assertThat(updatedCategory.name()).isEqualTo("수정된 카테고리");
        assertThat(updatedCategory.color()).isEqualTo("#fefefe");
        assertThat(updatedCategory.imageUrl()).isEqualTo("http://localhost:8080/updated.jpg");
        assertThat(updatedCategory.description()).isEqualTo("수정된 카테고리입니다.");
    }

    @Test
    @DisplayName("카테고리 수정 실패")
    void failUpdateCategory() {
        //given: 마지막 카테고리 수정
        var categories = getCategoryList();
        var lastCategory = categories.getLast();

        var reqbody = CategoryFixture.createCategory("", "", "", "수정 실패할 카테고리");
        var request = TestUtils.createRequestEntity("/api/categories/" + lastCategory.id(), reqbody, HttpMethod.PUT, accessToken);

        //when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(CommonErrorCode.INVALID_INPUT_VALUE.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo(CommonErrorCode.INVALID_INPUT_VALUE.getMessage());

        List<ValidationError> invalidParams = errorResponse.getInvalidParams();
        assertThat(invalidParams).hasSize(3);
        assertThat(invalidParams.parallelStream().map(ValidationError::message)).containsExactlyInAnyOrder(
                "카테고리 이름은 필수입니다.",
                "카테고리 색상은 필수입니다.",
                "카테고리 이미지 URL은 필수입니다."
        );
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() {
        //given: 마지막 카테고리 삭제
        var categories = getCategoryList();
        var lastCategoryId = categories.getLast().id();

        var url = baseUrl + "/api/categories/" + lastCategoryId;
        var request = TestUtils.createRequestEntity(url, null, HttpMethod.DELETE, accessToken);

        //when
        var actual = restTemplate.exchange(request, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo("카테고리를 삭제했습니다.");

        var deletedCategories = getCategoryList();
        assertThatList(deletedCategories).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 삭제 실패")
    void failDeleteCategory() {
        //given: 존재하지 않는 카테고리 삭제
        var categoryId = -1L;
        var request = TestUtils.createRequestEntity("/api/categories/" + categoryId, null, HttpMethod.DELETE, accessToken);

        //when
        var actual = restTemplate.exchange(request, ErrorResponse.class);
        ErrorResponse errorResponse = actual.getBody();

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse).isInstanceOf(ErrorResponse.class);

        assertThat(errorResponse.getCode()).isEqualTo(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode());
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getMessage()).isEqualTo(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
        assertThat(errorResponse.getInvalidParams()).isNull();
    }
}
