package gift.controller;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * webflux 를 이용한 webTestClient를 편하게 사용할 수 있습니다.<br> build.gradle 추가-> implementation
 * 'org.springframework.boot:spring-boot-starter-webflux'<br> (@SpringBootTest) 에서 Rest api test에
 * 사용할 수 있습니다.<br> url에 매개변수를 삽입 : uriMakeUseParameters로 url 생성 <br>
 * <p>
 * 사용예시<br>
 * <pre>{@code
 *     @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 *     class ControllerTest {
 *
 *     @LocalServerPort
 *     private int port;
 *     WebTestClientHelper webClient;
 *
 *     @BeforeEach
 *     void setUp() {
 *         webClient = new WebTestClientHelper(port);
 *     }
 *
 *     @Test
 *     @DisplayName("WebTestClientHelper 정상 작동 확인")
 *     void isOk() {
 *         //given
 *         String url = "/admin/products";
 *
 *         //when
 *         var response = webClient.get(url);
 *
 *         //then
 *         response.expectStatus().isOk();
 *         response.expectHeader().contentType("application/json");
 *         response.expectBody().json(...)
 *
 *     }
 *  }</pre>
 */
public class WebTestClientHelper {

    private final WebTestClient webTestClient;

    public WebTestClientHelper(int port) {
        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
            .build();
    }

    public ResponseSpec get(String url) {
        return webTestClient.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
    }

    public ResponseSpec post(String url, Object body) {
        return webTestClient.post().uri(url).accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body)).exchange();
    }

    public ResponseSpec put(String url, Object body) {
        return webTestClient.put().uri(url).accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body)).exchange();
    }

    public ResponseSpec delete(String url) {
        return webTestClient.delete().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
    }

    public WebTestClient moreAction() {
        return webTestClient;
    }

    /**
     * url을 만들어 준다.
     *
     * @param path       기본경로 ex)/api/products
     * @param parameters url 매개변수 ex) [["email" = "happy"],["password" = "1234"]]
     * @return String type 생성된 uri
     */
    public String uriMakeUseParameters(@NotNull String path, Map<String, String> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.path(path);
        parameters.forEach(builder::queryParam);
        return builder.build().toString();

    }
}
