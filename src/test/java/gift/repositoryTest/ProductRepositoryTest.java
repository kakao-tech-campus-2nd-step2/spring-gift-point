package gift.repositoryTest;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository products;
    @Autowired
    private CategoryRepository categories;

    @Test
    void save() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);
        Product expected = new Product("물건1", 2000L, "image.url", category);
        Product actual = products.save(expected);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
        Assertions.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        Assertions.assertThat(actual.getCategory()).isEqualTo(expected.getCategory());
    }

    @Test
    void findById() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);
        Product expected = new Product("물건1", 2000L, "image.url", category);
        products.save(expected);

        var actual = products.findById(expected.getId()).get();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllProducts() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);
        Product product1 = new Product("물건1", 2000L, "image.url", category);
        Product product2 = new Product("물건2", 2000L, "image.url", category);

        products.save(product1);
        products.save(product2);

        var actual = products.findAll();

        Assertions.assertThat(actual).hasSize(2);
        Assertions.assertThat(actual).contains(product1, product2);
    }

    @Test
    void updateProduct() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);
        Product product = new Product("물건1", 2000L, "image.url", category);
        products.save(product);

        product.updateProduct("수정된 물건", 2000L, "image.url", category);

        Assertions.assertThat(products.findById(product.getId()).get().getId()).isNotNull().isEqualTo(1L);
        Assertions.assertThat(products.findById(product.getId()).get()).isEqualTo(product);
        Assertions.assertThat(product.getName()).isEqualTo("수정된 물건");

    }

    @Test
    void deleteProduct() {
        Category category = new Category("교환권", "#6c95d1", "", "https://www.kakao.com");
        categories.save(category);
        Product product = new Product("product1", 2000L, "image.url", category);
        products.save(product);

        products.delete(product);
        Assertions.assertThat(products.findById(product.getId())).isNotPresent();
    }
}
