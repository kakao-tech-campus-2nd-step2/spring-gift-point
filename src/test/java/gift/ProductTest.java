package gift;

import gift.category.entity.Category;
import gift.controller.WebTestClientHelper;
import gift.option.dto.GiftOptionRequest;
import gift.product.dto.ProductRequest;
import gift.repository.JpaCategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTest {

    @LocalServerPort
    private int port;

    private JpaCategoryRepository jpaCategoryRepository;

    private WebTestClientHelper webTestClientHelper;

    private Category testCategory;

    private List<GiftOptionRequest> giftOptionRequests;

    public ProductTest(JpaCategoryRepository jpaCategoryRepository) {
        this.webTestClientHelper = new WebTestClientHelper(port);
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @BeforeEach
    void setUp() {
        //카테고리 저장
        testCategory = jpaCategoryRepository.save(
            new Category(null, "카테고리1", "red", "테스트카테고리임.", "abc.def"));

        //상품 옵션 저장
        giftOptionRequests = List.of(
            new GiftOptionRequest("옵션1", 1000),
            new GiftOptionRequest("옵션2", 2000),
            new GiftOptionRequest("옵션3", 3000)
        );
    }

    @Test
    @DisplayName("상품 추가 정상 동작")
    void test1() {
        //given
        String url = "/admin/products";

        //when
        ProductRequest productRequest = new ProductRequest("상품1", 1000, "abc", testCategory.getId(),
            giftOptionRequests);

        //then
        var response = webTestClientHelper.post(url, productRequest);

        response.expectStatus().isOk()
            .expectHeader().contentType("application/json")
            .expectBody().jsonPath("$.price", 1000);

    }
}
