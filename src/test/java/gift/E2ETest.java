package gift;

import gift.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class E2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
    }

    @Order(1)
    @Test
    void 유저_회원가입_로그인_성공_시나리오() {
        //회원가입
        MemberRegisterRequestDto request = new MemberRegisterRequestDto("test@gmail.com", "더미", "password");
        ResponseEntity<MemberRegisterResponseDto> response = restTemplate.postForEntity(baseUrl + "/members/signup", request, MemberRegisterResponseDto.class);

        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());

        //로그인
        MemberRequestDto requestDto = new MemberRequestDto("test@gmail.com", "password");
        ResponseEntity<TokenResponseDto> token = restTemplate.postForEntity(baseUrl + "/members/login", requestDto, TokenResponseDto.class);
        assertThat(HttpStatus.OK).isEqualTo(token.getStatusCode());
    }

    @Order(2)
    @Test
    void 카테고리_상품_등록_후_조회() {
        CategoryRequestDto request = new CategoryRequestDto("예시", "color", "test", "카테고리임");
        ResponseEntity<SuccessResponse> response = restTemplate.postForEntity(baseUrl + "/categories", request, SuccessResponse.class);
        System.out.println(response.getBody());
        ResponseEntity<List<CategoryResponseDto>> categoryResponse = restTemplate.exchange(
                baseUrl + "/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CategoryResponseDto>>() {
                });
        assertThat(categoryResponse.getBody()).isNotNull();

        ProductRequestDto productRequestDto = new ProductRequestDto("상품", "test.jpg",
                10000, 1L,
                List.of(new OptionRequestDto("옵션", 100)));
        ResponseEntity<SuccessResponse> productPostResponse = restTemplate.postForEntity(baseUrl + "/products", productRequestDto, SuccessResponse.class);
        assertThat(productPostResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Order(3)
    @Test
    void 로그인_위시_리스트_등록_후_조회() {
        //로그인
        MemberRequestDto requestDto = new MemberRequestDto("test@gmail.com", "password");
        ResponseEntity<TokenResponseDto> tokenResponse = restTemplate.postForEntity(baseUrl + "/members/login", requestDto, TokenResponseDto.class);
        //헤더에 토큰 삽입
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + tokenResponse.getBody().token();

        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        WishAddRequestDto wishRequest = new WishAddRequestDto(1L, 10);
        HttpEntity<WishAddRequestDto> entity = new HttpEntity<>(wishRequest, headers);
        ResponseEntity<SuccessResponse> wishPostResponse = restTemplate.postForEntity(
                baseUrl + "/wishes",
                entity,
                SuccessResponse.class);
        assertThat(wishPostResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
