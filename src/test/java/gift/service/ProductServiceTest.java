package gift.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.product.facadeRepository.ProductFacadeRepository;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaGiftOptionRepository;
import gift.product.dto.ProductRequest;
import gift.exceptionAdvisor.exceptions.GiftException;
import gift.category.entity.Category;
import gift.option.entity.GiftOption;
import gift.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductFacadeRepository productFacadeRepository;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private JpaGiftOptionRepository jpaGiftOptionRepository;


    @Test
    @DisplayName("상품 추가 테스트")
    void addProduct() {
        /*
        카테고리가 존재하는 상품을 추가할 때 정상적으로 등록이 된다.
         */
        // given
        Category category = new Category(null, "happy", "#000000", "happy", "happy.jpg");
        category = jpaCategoryRepository.save(category);

        GiftOption giftOption = new GiftOption(null, "happy", 1000);
        giftOption = jpaGiftOptionRepository.save(giftOption);

        ProductRequest productRequest = new ProductRequest(null, "happy", 1000, "happy.jpg",
            category.getId(), "happy", 1000);

        // when
        productService.create(productRequest);

        // then
        assertThat(productFacadeRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("카카오 이름을 사용하는 상품 추가 불가")
    void addProductWithKakaoName() {
        /*
        카카오 이름을 사용하는 상품을 추가할 때 예외가 발생한다.
         */
        // given
        Category category = new Category(null, "happy", "#000000", "happy", "happy.jpg");
        category = jpaCategoryRepository.save(category);

        GiftOption giftOption = new GiftOption(null, "happy", 1000);
        giftOption = jpaGiftOptionRepository.save(giftOption);

        ProductRequest productRequest = new ProductRequest(null, "카카오", 1000, "happy.jpg",
            category.getId(), "happy", 1000);

        // when
        // then
        assertThrows(GiftException.class, () -> productService.create(productRequest));
    }

}