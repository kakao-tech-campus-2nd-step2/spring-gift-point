package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.service.MemberService;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import gift.domain.wishlist.dto.ProductIdRequest;
import gift.domain.wishlist.entity.Wish;
import gift.domain.wishlist.repository.WishRepository;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.sql.init.mode=never"
})
class WishControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private String token;

    @BeforeEach
    public void beforeEach() {
        // 회원 가입
        MemberRequest memberRequest = new MemberRequest("test@google.co.kr", "password");
        token = memberService.register(memberRequest);
    }

    @Test
    @DisplayName("위시리스트 전체 조회 테스트")
    void getWishesTest() {
        // given
        var url = "http://localhost:" + port + "/api/wishes";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        // when
        var actual = restTemplate.exchange(requestEntity, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("위시리스트 생성 테스트")
    void createWishTest() {
        Category category = new Category("name", "color", "imageUrl", "description");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product("name", 1000, "imageUrl", savedCategory);
        productRepository.save(product);
        // given
        var request = new ProductIdRequest(1L);

        var url = "http://localhost:" + port + "/api/wishes";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var requestEntity = new RequestEntity<>(request, headers, HttpMethod.POST, URI.create(url));

        // when
        var actual = restTemplate.exchange(requestEntity, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    void deleteWishTest() {

        // given
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            Category category = new Category("name", "color", "imageUrl", "description");
            Category savedCategory = categoryRepository.save(category);

            Product product = new Product("name", 1000, "imageUrl", savedCategory);
            productRepository.save(product);
            Wish newWish = new Wish(memberService.getMemberFromToken(token), product);
            wishRepository.save(newWish);

            return null;
        });

        var id = 1L;
        var url = "http://localhost:" + port + "/api/wishes/" + id;
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var requestEntity = new RequestEntity<>(headers, HttpMethod.DELETE, URI.create(url));

        // when
        var actual = restTemplate.exchange(requestEntity, String.class);

        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}