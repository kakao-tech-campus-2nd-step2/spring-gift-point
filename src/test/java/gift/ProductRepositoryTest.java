package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository products;
    @Autowired
    private CategoryRepository categories;
    @Autowired
    private OptionRepository options;

    private final Category category = new Category(1L, "test_category", "000000",
        "category-image.url", "");

    @BeforeEach
    void setUp() {
        categories.save(category);
    }

    @Test
    void save() {
        var expected = createProduct(1L, "Product A", 4500, "image-productA.url");
        var actual = products.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    void find() {
        var expected = createProduct(1L, "Product A", 4500, "image-productA.url");
        var actual = products.save(expected);
        assertThat(actual.getName()).isEqualTo(
            products.findById(1L).orElseThrow(() -> new RepositoryException(
                ErrorCode.PRODUCT_NOT_FOUND, actual.getId())).getName());
    }

    @Test
    void findAll() {
        var product1 = createProduct(1L, "Product A", 4500, "image-productA.url");
        var product2 = createProduct(2L, "Product B", 5500, "image-productB.url");
        products.save(product1);
        products.save(product2);
        assertThat(products.findAll().size()).isEqualTo(2);
    }

    @Test
    void findPage() {
        var product1 = createProduct(1L, "Product A", 4500, "image-productA.url");
        var product2 = createProduct(2L, "Product B", 5500, "image-productB.url");
        var product3 = createProduct(3L, "Product C", 6500, "image-productC.url");
        products.save(product1);
        products.save(product2);
        products.save(product3);

        Pageable pageable1 = PageRequest.of(0, 2);
        Pageable pageable2 = PageRequest.of(1, 2);

        assertThat(products.findAll(pageable1).stream()
            .toList()
            .size())
            .isEqualTo(2);
        assertThat(products.findAll(pageable2).stream()
            .toList()
            .size())
            .isEqualTo(1);
    }

    @Test
    void update() {
        var expected = createProduct(1L, "Product A", 4500, "image-productA.url");
        products.save(expected);
        var current = products.findById(1L)
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, 1L));
        var update = updateProduct(current, 5500);
        products.save(update);

        assertThat(update.getPrice()).isEqualTo(5500);
    }

    @Test
    void delete() {
        var product = createProduct(1L, "Product A", 4500, "image-productA.url");
        products.save(product);
        products.deleteById(product.getId());
        assertThat(products.findAll()).isEmpty();
    }

    private Product updateProduct(Product currentProduct, long newPrice) {
        return createProduct(currentProduct.getId(), currentProduct.getName(), newPrice,
            currentProduct.getImageUrl());
    }

    private Product createProduct(long id, String name, long price, String imageUrl) {
        return new Product(id, name, price, imageUrl, category);
    }
}
