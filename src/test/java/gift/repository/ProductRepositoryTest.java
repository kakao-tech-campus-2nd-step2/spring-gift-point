package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import gift.product.model.Category;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void 상품_추가() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));

        //when
        Product insertedProduct = productRepository.save(
            new Product("테스트1", 1500, "테스트주소1", category));

        //then
        assertSoftly(softly -> {
            assertThat(insertedProduct.getId()).isNotNull();
            assertThat(insertedProduct.getName()).isEqualTo("테스트1");
            assertThat(insertedProduct.getPrice()).isEqualTo(1500);
            assertThat(insertedProduct.getImageUrl()).isEqualTo("테스트주소1");
            assertThat(insertedProduct.getCategory().getName()).isEqualTo("테스트카테고리1");
        });
    }

    @Test
    void 상품_전체_조회() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));
        productRepository.save(new Product("테스트2", 3000, "테스트주소2", category));

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThat(products).hasSize(2);
    }

    @Test
    void 상품_조회() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));

        //when
        boolean isPresentProduct = productRepository.findById(product.getId()).isPresent();

        //then
        assertThat(isPresentProduct).isTrue();
    }

    @Test
    void 상품_수정() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));

        //when
        Product updatedProduct = productRepository.save(
            new Product(product.getId(), product.getName(), 9999,
                product.getImageUrl(), category));

        //then
        assertThat(updatedProduct.getPrice()).isEqualTo(9999);
    }

    @Test
    void 상품_삭제() {
        //given
        Category category = categoryRepository.save(new Category("테스트카테고리1"));
        Product product = productRepository.save(new Product("테스트1", 1500, "테스트주소1", category));

        //when
        productRepository.deleteById(product.getId());
        boolean isPresentProduct = productRepository.findById(product.getId()).isPresent();

        //then
        assertThat(isPresentProduct).isFalse();
    }
}
