package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.category.dto.request.CreateCategoryRequest;
import gift.product.category.dto.response.CategoryResponse;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/initialize.sql",
    "/sql/insert_categories.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CategoryTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void Port() {
        assertThat(port).isNotZero();
    }

    @Test
    @DisplayName("get all categories test")
    void getAllCategoriesTest() {
        // given
        var url = "http://localhost:" + port + "/api/categories";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getBody()).isNotNull();
    }

    @Test
    @DisplayName("get id 1L category test")
    void getId1LCategoryTest() {
        // given
        var url = "http://localhost:" + port + "/api/categories/1";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, CategoryResponse.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().id()).isEqualTo(1L);
        assertThat(actualResponse.getBody().name()).isEqualTo("Category 1");
    }

    @Test
    @DisplayName("get category by not exist id test")
    void getCategoryByNotExistIdTest() {
        // given
        var url = "http://localhost:" + port + "/api/categories/999";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, CategoryResponse.class);

        // then
        assertThat(actualResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("create category test")
    void createCategoryTest() {
        // given
        var createBody = new CreateCategoryRequest("Category 4", "#123456", "image", "");
        var url = "http://localhost:" + port + "/api/categories";
        var request = new RequestEntity<>(createBody, HttpMethod.POST, URI.create(url));

        var expectedLocation = URI.create("/api/categories/4");

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getHeaders().getLocation()).isEqualTo(expectedLocation);
        assertThat(actualResponse.getBody()).isNotNull();
    }

    @Test
    @DisplayName("create exist category test")
    void createExistCategoryTest() {
        // given
        var createBody = new CreateCategoryRequest("Category 1", "#123456", "image", "");
        var url = "http://localhost:" + port + "/api/categories";
        var request = new RequestEntity<>(createBody, HttpMethod.POST, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("delete category test")
    void deleteCategoryTest() {
        // given
        var url = "http://localhost:" + port + "/api/categories/1";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(actualResponse.getBody()).isNull();
    }

    @Test
    @DisplayName("delete not exist category test")
    void deleteNotExistCategoryTest() {
        // given
        var url = "http://localhost:" + port + "/api/categories/999";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var actualResponse = restTemplate.exchange(request, String.class);

        // then
        assertThat(actualResponse.getStatusCode().is4xxClientError()).isTrue();
    }
}
