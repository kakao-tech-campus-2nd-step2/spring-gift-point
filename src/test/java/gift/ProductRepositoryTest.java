package gift;

import gift.domain.Category;
import gift.domain.CategoryName;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveProduct() {
        Category category = categoryRepository.findByName(CategoryName.교환권)
            .orElseGet(() -> categoryRepository.save(new Category.CategoryBuilder()
                .name(CategoryName.교환권)
                .color("#6c95d1")
                .imageUrl("https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png")
                .description("")
                .build()));
        Product expected = new Product.ProductBuilder()
            .name("Product1")
            .price(BigDecimal.valueOf(10.00))
            .imageUrl("http://example.com/product1.jpg")
            .description("Description for Product1")
            .category(category)
            .build();
        Product actual = productRepository.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}
