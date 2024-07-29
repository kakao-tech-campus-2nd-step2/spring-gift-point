package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.controller.auth.AuthMapper;
import gift.controller.auth.LoginRequest;
import gift.controller.auth.Token;
import gift.controller.product.ProductRequest;
import gift.controller.product.ProductResponse;
import gift.controller.wish.WishCreateRequest;
import gift.controller.wish.WishResponse;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.util.JwtUtil;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ApplicationContext context;
    @LocalServerPort
    private int port;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;

//    void scenarioTest() {
//        // register
//        String url = "http://localhost:" + port + "/api/members/register";
//        LoginRequest loginRequest = new LoginRequest("hongsik1234@kakao.com", "password1234");
//        RequestEntity<LoginRequest> loginRequestEntity = new RequestEntity<>(loginRequest,
//            HttpMethod.POST, URI.create(url));
//        ResponseEntity<Token> registerResponse = restTemplate.exchange(loginRequestEntity,
//            Token.class);
//        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        // login
//        url = "http://localhost:" + port + "/login";
//        loginRequestEntity = new RequestEntity<>(loginRequest, HttpMethod.POST, URI.create(url));
//        ResponseEntity<Token> loginResponse = restTemplate.exchange(loginRequestEntity,
//            Token.class);
//        String token = loginResponse.getBody().token();
//        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // product create
//        url = "http://localhost:" + port + "/api/products";
//        ProductRequest productRequest = new ProductRequest("testProduct", 2000L,
//            "kakao.com/productTest");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        RequestEntity<ProductRequest> productRequestRequestEntity = new RequestEntity<>(
//            productRequest, headers, HttpMethod.POST, URI.create(url));
//        ResponseEntity<ProductResponse> productResponse = restTemplate.exchange(
//            productRequestRequestEntity, ProductResponse.class);
//        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        // product get
//        url = "http://localhost:" + port + "/api/products";
//        RequestEntity<Void> voidRequestEntity = new RequestEntity<>(null, headers, HttpMethod.GET,
//            URI.create(url));
//        ResponseEntity<Page<ProductResponse>> productsResponse = restTemplate.exchange(
//            voidRequestEntity, new ParameterizedTypeReference<Page<ProductResponse>>() {
//            });
//        assertThat(productsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // wish create
//        String email = JwtUtil.verifyToken(new Token(token)).getSubject();
//        UUID memberId = AuthMapper.toLoginResponse(memberService.findByEmail(email)).id();
//        UUID productId = productsResponse.getBody().getContent().get(0).id();
//        url = "http://localhost:" + port + "/api/wishes/" + memberId;
//        RequestEntity<WishCreateRequest> wishRequestEntity = new RequestEntity<>(
//            new WishCreateRequest(productId, 3L), headers, HttpMethod.POST, URI.create(url));
//        ResponseEntity<WishResponse> wishResponse = restTemplate.exchange(wishRequestEntity,
//            WishResponse.class);
//        assertThat(wishResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        // wish get
//        url = "http://localhost:" + port + "/api/wishes/" + memberId;
//        voidRequestEntity = new RequestEntity<>(null, headers, HttpMethod.GET, URI.create(url));
//        ResponseEntity<List<WishResponse>> wishesResponse = restTemplate.exchange(voidRequestEntity,
//            new ParameterizedTypeReference<List<WishResponse>>() {
//            });
//        assertThat(wishesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
}