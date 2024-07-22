package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.global.exception.custrom.NotFoundException;
import gift.member.business.dto.JwtToken;
import gift.global.authentication.jwt.JwtValidator;
import gift.global.authentication.jwt.TokenType;
import gift.member.persistence.repository.MemberRepository;
import gift.member.persistence.repository.WishlistRepository;
import gift.member.presentation.dto.RequestMemberDto;
import gift.member.presentation.dto.RequestWishlistDto;
import gift.member.presentation.dto.ResponsePagingWishlistDto;
import gift.product.persistence.entity.Category;
import gift.product.persistence.entity.Product;
import gift.product.persistence.repository.CategoryJpaRepository;
import gift.product.persistence.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtValidator jwtValidator;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private static String accessToken;
    private static String refreshToken;

    @BeforeAll
    static void setUp(@Autowired TestRestTemplate restTemplate,
        @LocalServerPort int port,
        @Autowired CategoryJpaRepository categoryJpaRepository,
        @Autowired ProductRepository productRepository
    ) {
        RequestMemberDto requestMemberDto = new RequestMemberDto(
            "test@example.com",
            "test");

        String url = "http://localhost:" + port + "/api/members/register";
        var response = restTemplate.postForEntity(url, requestMemberDto, JwtToken.class);
        var jwtToken = response.getBody();
        accessToken = jwtToken.accessToken();
        refreshToken = jwtToken.refreshToken();

        // 카테고리 추가
        var category = categoryJpaRepository.save(new Category("test"));

        // 상품 추가
        Product product = new Product("test", "description", 1000, "http://test.com", category);
        productRepository.saveProduct(product);
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteAll();
    }

    @Test
    void testRegisterMember() {
        //given
        RequestMemberDto requestMemberDto = new RequestMemberDto(
            "test1@example.com",
            "test");

        String url = "http://localhost:" + port + "/api/members/register";

        //when
        var response = restTemplate.postForEntity(url, requestMemberDto, JwtToken.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var jwtToken = response.getBody();
        var id = jwtValidator.validateAndParseToken("Bearer " + jwtToken.accessToken(), TokenType.ACCESS);
        var member = memberRepository.getMemberById(id);

        assertThat(member.getEmail()).isEqualTo(requestMemberDto.email());
    }

    @Test
    void testLoginMember() {
        //given
        RequestMemberDto requestMemberDto = new RequestMemberDto(
            "test@example.com",
            "test");

        String url = "http://localhost:" + port + "/api/members/login";

        //when
        var response = restTemplate.postForEntity(url, requestMemberDto, JwtToken.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var jwtToken = response.getBody();
        var id = jwtValidator.validateAndParseToken("Bearer " + jwtToken.accessToken(), TokenType.ACCESS);
        var member = memberRepository.getMemberById(id);

        assertThat(member.getEmail()).isEqualTo(requestMemberDto.email());
    }

    @Test
    void testReissueRefreshToken() {
        //given
        String url = "http://localhost:" + port + "/api/members/reissue";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(refreshToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var reissuedTokenId = jwtValidator.validateAndParseToken("Bearer " + response.getBody(), TokenType.ACCESS);
        var originalTokenId = jwtValidator.validateAndParseToken("Bearer " + accessToken, TokenType.ACCESS);
        assertThat(reissuedTokenId).isEqualTo(originalTokenId);
    }

    @Test
    void testAddAndGetWishLists() {
        // when
        String url = "http://localhost:" + port + "/api/members/wishlists/products/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Long> response1 = restTemplate.exchange(url, HttpMethod.POST, entity, Long.class);

        //then
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //given
        url = "http://localhost:" + port + "/api/members/wishlists?page=0";
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<ResponsePagingWishlistDto> response2 = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            ResponsePagingWishlistDto.class
        );

        //then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        var responseWishListDto = response2.getBody();
        assertThat(responseWishListDto.wishlistList().getFirst().productName()).isEqualTo("test");
    }

    @Test
    void testUpdateWishList() {
        // given
        String url = "http://localhost:" + port + "/api/members/wishlists/products/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Long> response1 = restTemplate.exchange(url, HttpMethod.POST, entity1, Long.class);

        // when
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        RequestWishlistDto requestWishlistDto = new RequestWishlistDto(2);

        HttpEntity<RequestWishlistDto> entity2 = new HttpEntity<>(requestWishlistDto, headers);

        ResponseEntity<Long> response2 = restTemplate.exchange(url, HttpMethod.PUT, entity2, Long.class);

        // then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        var wishlist = wishlistRepository.getWishListByMemberIdAndProductId(1L, 1L);
        assertThat(wishlist.getCount()).isEqualTo(2);
    }

    @Test
    void testDeleteWishList() {
        // given
        String url = "http://localhost:" + port + "/api/members/wishlists/products/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity1 = new HttpEntity<>(headers);

        ResponseEntity<Long> response1 = restTemplate.exchange(url, HttpMethod.POST, entity1, Long.class);

        // when
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity2 = new HttpEntity<>(headers);

        ResponseEntity<Long> response2 = restTemplate.exchange(url, HttpMethod.DELETE, entity2, Long.class);

        // then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThrows(NotFoundException.class,
            () -> wishlistRepository.getWishListByMemberIdAndProductId(1L, 1L));
    }

    @Test
    void testAddAndGetWishLists_Fail() {
        //given
        String url = "http://localhost:" + port + "/api/members/wishlists?page=0&size=101";

        // when
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var entity = new HttpEntity<>(headers);
        var response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().contains("size는 1~100 사이의 값이어야 합니다.")).isTrue();
    }
}
