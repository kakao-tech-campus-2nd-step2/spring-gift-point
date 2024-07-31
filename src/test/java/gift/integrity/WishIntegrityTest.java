package gift.integrity;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.dto.auth.AccountDto;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.category.CategoryDto;
import gift.product.dto.product.ClientProductDto;
import gift.product.dto.product.ProductDto;
import gift.product.dto.wish.WishDto;
import gift.product.service.AuthService;
import gift.product.service.CategoryService;
import gift.product.service.ProductService;
import gift.product.service.WishService;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
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
class WishIntegrityTest {

    static final String BASE_URL = "http://localhost:";
    @LocalServerPort
    int port;
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AuthService authService;

    @Autowired
    ProductService productService;

    @Autowired
    WishService wishService;

    @Autowired
    CategoryService categoryService;

    String accessToken;

    @BeforeAll
    void 멤버_및_상품_셋팅() {
        MemberDto memberDto = new MemberDto("test_name", "test@test.com", "1234");
        authService.register(memberDto);
        accessToken = authService.login(new AccountDto(memberDto.email(), memberDto.password())).accessToken();

        CategoryDto categoryDto = new CategoryDto("테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        categoryService.insertCategory(categoryDto);

        String url = BASE_URL + port + "/api/products";
        ProductDto productDto = new ClientProductDto("테스트1", 1500, "테스트주소1", categoryDto.name());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<ProductDto> requestEntity = new RequestEntity<>(productDto, headers,HttpMethod.POST,
            URI.create(url));

        testRestTemplate.exchange(requestEntity, String.class);
    }

    @Order(1)
    @Test
    void 위시리스트_추가() {
        //given
        String url = BASE_URL + port + "/api/wishes";
        WishDto wishDto = new WishDto(1L);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<WishDto> requestEntity = new RequestEntity<>(wishDto, headers,
            HttpMethod.POST, URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Order(2)
    @Test
    void 위시리스트_전체_조회() {
        //given
        String url = BASE_URL + port + "/api/wishes";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<WishDto> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(3)
    @Test
    void 위시리스트_조회() {
        //given
        String url = BASE_URL + port + "/api/wishes/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<WishDto> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(4)
    @Test
    void 위시리스트_삭제() {
        //given
        String url = BASE_URL + port + "/api/wishes/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        RequestEntity<WishDto> requestEntity = new RequestEntity<>(headers, HttpMethod.DELETE,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
