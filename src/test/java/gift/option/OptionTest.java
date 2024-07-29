package gift.option;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptionTest {

    @LocalServerPort
    private int port;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    void setUp() {

        url = "http://localhost:" + port;

        Member member = new Member(2L,"admin2@kakao.com", "2222");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Member> requestEntity = new HttpEntity<>(member, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/members/login", member, String.class);

        int startIndex = responseEntity.getBody().indexOf("\"token\":\"") + "\"token\":\"".length();
        int endIndex = responseEntity.getBody().indexOf("\"", startIndex);
        token = responseEntity.getBody().substring(startIndex, endIndex);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("옵션 추가")
    @DirtiesContext
    void addOption() {
        Option option = new Option("새옵션",4444L);

        HttpEntity<Option> requestEntity = new HttpEntity<>(option, getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1/options", HttpMethod.POST,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("옵션 조회")
    @DirtiesContext
    void getAllOption() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1/options", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("옵션 단일 조회")
    @DirtiesContext
    void getOptionById() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1/options/1", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("옵션 수정")
    @DirtiesContext
    void updateOption() {
        Option option = new Option("Option4",5555L);

        HttpEntity<Option> requestEntity = new HttpEntity<>(option, getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1/options/3", HttpMethod.PUT,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("옵션 삭제")
    @DirtiesContext
    void deleteOption() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/products/1/options/3", HttpMethod.DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
