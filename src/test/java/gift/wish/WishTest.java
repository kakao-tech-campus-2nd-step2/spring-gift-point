package gift.wish;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish.WishRequest;
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
public class WishTest {

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
    @DisplayName("위시리스트 추가")
    @DirtiesContext
    void addWishList() {
        WishRequest request = new WishRequest(3L);

        HttpEntity<WishRequest> requestEntity = new HttpEntity<>(request, getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/wish", HttpMethod.POST,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시리스트 조회")
    @DirtiesContext
    void getWishList() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/wish", HttpMethod.GET,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("위시리스트 삭제")
    @DirtiesContext
    void deleteWishList() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/api/wish/4", HttpMethod.DELETE,
            requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
