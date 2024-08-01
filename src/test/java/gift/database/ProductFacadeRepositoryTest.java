package gift.database;

import static org.junit.jupiter.api.Assertions.*;

import gift.repository.JpaCategoryRepository;
import gift.repository.JpaGiftOptionRepository;
import gift.repository.JpaProductRepository;
import gift.exceptionAdvisor.exceptions.GiftException;
import gift.category.entity.Category;
import gift.option.entity.GiftOption;
import gift.product.entity.Product;
import gift.product.facadeRepository.ProductFacadeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductFacadeRepositoryTest {

    @Autowired
    private ProductFacadeRepository productFacadeRepository;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private JpaGiftOptionRepository jpaGiftOptionRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Test
    @DisplayName("카테고리가 존재할 때 새로운 상품과 옵션을 만들어낸다.")
    void test1() {
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category = jpaCategoryRepository.save(category);
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", category, giftOption);

        product.addGiftOption(giftOption);
        // when
        productFacadeRepository.saveProduct(product);

        // then
        assertNotNull(jpaProductRepository.findById(product.getId()).orElseThrow());
        assertNotNull(jpaGiftOptionRepository.findById(giftOption.getId()).orElseThrow());

    }

    @Test
    @DisplayName("카테고리가 존재하지 않을 때 예외를 던진다.")
    void test2() {
        // given
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", null, giftOption);

        product.addGiftOption(giftOption);


        // when & then
        assertThrows(GiftException.class, () -> productFacadeRepository.saveProduct(product));

    }

    @Test
    @DisplayName("상품 여러개를 조회한다.")
    void test3() {
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category = jpaCategoryRepository.save(category);
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", category, giftOption);

        product.addGiftOption(giftOption);
        productFacadeRepository.saveProduct(product);

        // when
        Product product2 = new Product(null, "test2", 123, "abc", category, giftOption);
        product2.addGiftOption(giftOption);
        productFacadeRepository.saveProduct(product2);

        // then
        assertEquals(2, productFacadeRepository.findAll().size());
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void test4() {
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category = jpaCategoryRepository.save(category);
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", category, giftOption);

        product.addGiftOption(giftOption);
        productFacadeRepository.saveProduct(product);

        // when
        productFacadeRepository.deleteProduct(product.getId());

        // then
        assertEquals(0, productFacadeRepository.findAll().size());
    }

    @Test
    @DisplayName("상품을 수정한다.")
    void test5() {
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category = jpaCategoryRepository.save(category);
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", category, giftOption);

        product.addGiftOption(giftOption);
        product = productFacadeRepository.saveProduct(product);

        // when
        Product product2 = new Product(product.getId(), "test2", 123, "abc", category, giftOption);
        product2.addGiftOption(giftOption);
        productFacadeRepository.saveProduct(product2);

        // then
        assertEquals("test2", productFacadeRepository.getProduct(product2.getId()).getName());
        assertEquals(1, productFacadeRepository.findAll().size());
        assertEquals(123, productFacadeRepository.getProduct(product2.getId()).getPrice());
    }

    @Test
    @DisplayName("상품을 조회했을 때 연관된 카테고리와 옵션이 조회된다.")
    void test6() {
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category = jpaCategoryRepository.save(category);
        GiftOption giftOption = new GiftOption(null, "test", 123);

        Product product = new Product(null, "test", 123, "abc", category, giftOption);

        product.addGiftOption(giftOption);
        product = productFacadeRepository.saveProduct(product);

        // when
        Product product2 = productFacadeRepository.getProduct(product.getId());

        // then
        assertNotNull(product2.getCategory());
        assertNotNull(product2.getGiftOptionList());
        assertEquals(category.getName(), product2.getCategory().getName());
    }

}