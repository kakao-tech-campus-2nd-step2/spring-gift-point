package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.option.dto.request.CreateOptionRequest;
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
@Sql(scripts = {"/sql/initialize.sql", "/sql/insert_categories.sql",
    "/sql/insert_products.sql",
    "/sql/insert_options.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OptionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void Port() {
        assertThat(port).isNotZero();
    }

    @Test
    @DisplayName("get all options of product 1L")
    void getAllOptionsOfProduct1L() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);
        System.out.println(response);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("add option to product test")
    void addOptionToProductTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options";
        var requestBody = new CreateOptionRequest("new option", 1000);
        var request = new RequestEntity<>(requestBody, HttpMethod.POST, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("add option duplicate name test")
    void addOptionDuplicateNameTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options";
        var requestBody = new CreateOptionRequest("Option A1", 1000);
        var request = new RequestEntity<>(requestBody, HttpMethod.POST, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("add option quantity over 1 million")
    void addOptionQuantityOver1Million() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options";
        var requestBody = new CreateOptionRequest("new option", 100_000_000);
        var request = new RequestEntity<>(requestBody, HttpMethod.POST, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);
        System.out.println(response);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("add option quantity under 1")
    void addOptionQuantityUnder1() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options";
        var requestBody = new CreateOptionRequest("new option", 100_000_000);
        var request = new RequestEntity<>(requestBody, HttpMethod.POST, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    @DisplayName("delete test")
    void deleteTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/1/options/1";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    @DisplayName("delete last option test")
    void deleteLastOptionTest() {
        // given
        var url = "http://localhost:" + port + "/api/products/5/options/9";
        var request = new RequestEntity<>(HttpMethod.DELETE, URI.create(url));

        // when
        var response = restTemplate.exchange(request, String.class);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
