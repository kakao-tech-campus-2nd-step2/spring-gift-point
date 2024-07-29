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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

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
        wishRepository.saveAll(wishes);
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
        String url = "http://localhost:" + port + "/api/wishlist";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        RequestEntity<Void> request = new RequestEntity<>(
            headers, HttpMethod.GET, URI.create(url));

        //when
        ResponseEntity<List<ProductResponse>> response
            = restTemplate.exchange(request,
            new ParameterizedTypeReference<>() {
            });

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        IntStream.range(0, 5)
            .forEach(i -> {
                ProductResponse pr = response.getBody().get(i);
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
        ResponseEntity<Void> response = addAndRemoveTest(saved.getId(), HttpMethod.POST);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시 리스트 추가 실패 테스트")
    void failAddMyWish() {
        ResponseEntity<Void> response = addAndRemoveTest(savedProducts.get(0).getId(),
            HttpMethod.POST);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("위시 리스트 삭제 성공 테스트")
    void removeMyWish() {
        ResponseEntity<Void> response = addAndRemoveTest(savedProducts.get(0).getId(),
            HttpMethod.DELETE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("위시 리스트 삭제 실패 테스트")
    void failRemoveMyWish() {
        Long nonExistingId = 1239L; //위시 리스트에 존재하지 않는 상품 id
        ResponseEntity<Void> response = addAndRemoveTest(nonExistingId, HttpMethod.DELETE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<Void> addAndRemoveTest(Long productId, HttpMethod httpMethod) {
        //given
        String url = "http://localhost:" + port + "/api/wishlist";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        WishListRequest wishListRequest = new WishListRequest(productId);

        RequestEntity<WishListRequest> request = new RequestEntity<>(
            wishListRequest, headers, httpMethod, URI.create(url));

        //when
        return restTemplate.exchange(request, Void.class);

    }

}