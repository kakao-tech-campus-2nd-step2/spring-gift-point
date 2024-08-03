package gift.integrity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.option.OptionDto;
import gift.product.model.Category;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.ProductRepository;
import gift.product.service.AuthService;
import java.net.URI;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OptionIntegrityTest {

    static final String BASE_URL = "http://localhost:";

    String accessToken;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuthService authService;

    static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of("실패 옵션 이름이 50자를 초과했을 때",
                "테스트옵션".repeat(51),
                1,
                "옵션 이름은 공백 포함 최대 50자까지 입력할 수 있습니다."),
            Arguments.of("실패 옵션 이름에 사용 불가능한 특수 문자를 입력했을 때",
                "테스트옵션#",
                1,
                "사용 가능한 특수 문자는 ()[]+-&/_ 입니다."),
            Arguments.of("실패 옵션 수량이 범위를 초과했을 때",
                "테스트옵션",
                100_000_001,
                "옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.")
        );
    }

    @BeforeAll
    void 로그인() {
        MemberDto memberDto = new MemberDto("test@test.com", "1234");
        authService.register(memberDto);
        accessToken = authService.login(new MemberDto(memberDto.email(), memberDto.password()))
            .accessToken();
    }

    @Order(1)
    @Test
    void 옵션_추가() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리",
            "테스트컬러",
            "테스트주소",
            "테스트설명"));
        Product product = productRepository.save(new Product("테스트상품", 1500, "테스트주소", category));
        String url = BASE_URL + port + "/api/products/" + product.getId() + "/options";
        OptionDto optionDto = new OptionDto("테스트옵션1", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(optionDto,
            headers,
            HttpMethod.POST,
            URI.create(url));

        //when
        var actual1 = testRestTemplate.exchange(requestEntity, String.class);
        optionDto = new OptionDto("테스트옵션2", 1);

        requestEntity = new RequestEntity<>(optionDto, headers, HttpMethod.POST,
            URI.create(url));
        var actual2 = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertSoftly(softly -> {
            assertThat(actual1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(actual2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        });
    }

    @Order(2)
    @Test
    void 옵션_조회() {
        //given
        String url = BASE_URL + port + "/api/options/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(3)
    @Test
    void 옵션_전체_조회() {
        //given
        String url = BASE_URL + port + "/api/options";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(3)
    @Test
    void 특정_상품의_옵션_전체_조회() {
        //given
        String url = BASE_URL + port + "/api/products/1/options";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(4)
    @Test
    void 옵션_수정() {
        //given
        String url = BASE_URL + port + "/api/products/1/options/1";
        OptionDto updatedOptionDto = new OptionDto("테스트옵션수정", 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(updatedOptionDto,
            headers, HttpMethod.PUT, URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Order(5)
    @Test
    void 옵션_삭제() {
        //given
        String url = BASE_URL + port + "/api/products/1/options/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(headers, HttpMethod.DELETE,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("generateTestData")
    @Order(6)
    void 실패_옵션_잘못된_값으로_요청(String testName,
        String testOptionName,
        int testQuantity,
        String errorMessage) throws JsonProcessingException {
        //given
        String url = BASE_URL + port + "/api/products/1/options";
        OptionDto optionDto = new OptionDto(testOptionName, testQuantity);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<OptionDto> requestEntity = new RequestEntity<>(optionDto,
            headers,
            HttpMethod.POST,
            URI.create(url));

        //when
        ObjectMapper mapper = new ObjectMapper();
        String responseMessage = testRestTemplate.exchange(requestEntity, String.class).getBody();
        Map<String, Object> responseMessageMap = mapper.readValue(responseMessage, Map.class);

        String message = (String) responseMessageMap.get("message");

        //then
        assertThat(message).isEqualTo(errorMessage);
    }
}