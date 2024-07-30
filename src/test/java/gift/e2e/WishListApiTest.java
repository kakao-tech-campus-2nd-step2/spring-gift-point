package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import gift.auth.JwtTokenProvider;
import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import gift.request.WishListRequest;
import gift.response.ProductListResponse;
import gift.response.ProductResponse;
import gift.service.MemberService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WishListApiTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WishRepository wishRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Member member;
    String token;
    List<Product> savedProducts;
    Category category;
    List<Wish> savedWishes;

    @BeforeEach
    void before() {
        category = categoryRepository.save(new Category("카테고리"));
        List<Product> products = new ArrayList<>();
        IntStream.range(0, 10)
            .forEach(i -> {
                products.add(new Product("product" + i, 1000, "https://a.com", category));
            });
        savedProducts = productRepository.saveAll(products);

        member = memberService.join("aaa123@a.com", "1234");
        token = jwtTokenProvider.generateToken(member);

        List<Wish> wishes = products.stream()
            .map(product -> new Wish(member, product))
            .toList();
        savedWishes = wishRepository.saveAll(wishes);
    }

    @AfterEach
    void after() {
        wishRepository.deleteAll();
        memberRepository.delete(member);
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("위시 리스트 조회 테스트")
    void getWishList() {
        //given
        String url = "http://localhost:" + port + "/api/wishes";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        RequestEntity<Void> request = new RequestEntity<>(
            headers, HttpMethod.GET, URI.create(url));

        //when
        ResponseEntity<ProductListResponse> response
            = restTemplate.exchange(request,
            new ParameterizedTypeReference<>() {
            });

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contents()).hasSize(10);
        IntStream.range(0, 5)
            .forEach(i -> {
                ProductResponse pr = response.getBody().contents().get(i);
                assertThat(pr.id()).isEqualTo(savedProducts.get(i).getId());
                assertThat(pr.name()).isEqualTo(savedProducts.get(i).getName());
                assertThat(pr.price()).isEqualTo(savedProducts.get(i).getPrice());
                assertThat(pr.imageUrl()).isEqualTo(savedProducts.get(i).getImageUrl());
                assertThat(pr.categoryName()).isEqualTo(savedProducts.get(i).getCategoryName());
            });
    }


    @Test
    @DisplayName("위시 리스트 추가 성공 테스트")
    void addMyWish() {
        Product saved = productRepository.save(new Product("product11", 1000, "https://a.com", category));
        ResponseEntity<Void> response = addTest(saved.getId(), HttpMethod.POST);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시 리스트 추가 실패 테스트")
    void failAddMyWish() {
        ResponseEntity<Void> response = addTest(savedProducts.get(0).getId(),
            HttpMethod.POST);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("위시 리스트 삭제 성공 테스트")
    void removeMyWish() {
        Long wishId = savedWishes.get(0).getId();
        ResponseEntity<Void> response = removeTest(wishId,
            HttpMethod.DELETE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("위시 리스트 삭제 실패 테스트")
    void failRemoveMyWish() {
        Long nonExistingId = 1239L;
        ResponseEntity<Void> response = removeTest(nonExistingId, HttpMethod.DELETE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Void> addTest(Long productId, HttpMethod httpMethod) {
        //given
        String url = "http://localhost:" + port + "/api/wishes";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        WishListRequest wishListRequest = new WishListRequest(productId);

        RequestEntity<WishListRequest> request = new RequestEntity<>(
            wishListRequest, headers, httpMethod, URI.create(url));

        //when
        return restTemplate.exchange(request, Void.class);

    }

    private ResponseEntity<Void> removeTest(Long wishId, HttpMethod httpMethod) {
        //given
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:" + port)
            .path("/api/wishes/{wishId}")
            .encode()
            .buildAndExpand(wishId)
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        RequestEntity<WishListRequest> request = new RequestEntity<>(
            headers, httpMethod, uri);

        //when
        return restTemplate.exchange(request, Void.class);

    }

}