package gift.integrity;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.dto.category.CategoryDto;
import java.net.URI;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryIntegrityTest {

    static final String BASE_URL = "http://localhost:";
    @LocalServerPort
    int port;
    @Autowired
    TestRestTemplate testRestTemplate;

    @Order(1)
    @Test
    void 카테고리_추가() {
        //given
        String url = BASE_URL + port + "/api/categories";
        CategoryDto categoryDto = new CategoryDto("테스트카테고리1");

        RequestEntity<CategoryDto> requestEntity = new RequestEntity<>(categoryDto, HttpMethod.POST,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Order(2)
    @Test
    void 카테고리_전체_조회() {
        //given
        String url = BASE_URL + port + "/api/categories";

        RequestEntity<CategoryDto> requestEntity = new RequestEntity<>(HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(3)
    @Test
    void 카테고리_조회() {
        //given
        String url = BASE_URL + port + "/api/categories/1";

        RequestEntity<CategoryDto> requestEntity = new RequestEntity<>(HttpMethod.GET,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(4)
    @Test
    void 카테고리_수정() {
        //given
        String url = BASE_URL + port + "/api/categories/1";
        CategoryDto categoryDto = new CategoryDto("테스트카테고리2");

        RequestEntity<CategoryDto> requestEntity = new RequestEntity<>(categoryDto, HttpMethod.PUT,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(5)
    @Test
    void 카테고리_삭제() {
        //given
        String url = BASE_URL + port + "/api/categories";
        CategoryDto categoryDto = new CategoryDto("테스트카테고리1");
        RequestEntity<CategoryDto> requestEntity = new RequestEntity<>(categoryDto, HttpMethod.POST,
            URI.create(url));
        testRestTemplate.exchange(requestEntity, String.class);

        url = BASE_URL + port + "/api/categories/1";
        requestEntity = new RequestEntity<>(HttpMethod.DELETE,
            URI.create(url));

        //when
        var actual = testRestTemplate.exchange(requestEntity, String.class);

        //then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
