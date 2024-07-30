package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OptionRepositoryTest {

    @Autowired
    OptionRepository options;
    @Autowired
    ProductRepository products;
    @Autowired
    CategoryRepository categories;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        var category = new Category(1L, "category", "color", "imageUrl", "");
        categories.save(category);
        product = new Product(1L, "product", 4500, "imageUrl", category);
        products.save(product);
    }

    @Test
    void save() {
        var option = new Option("option", 2);
        option.setProduct(product);
        options.save(option);
        assertThat(option.getProduct()
            .getName())
            .isEqualTo("product");
    }

    @Test
    void subtract() {
        var option = new Option("option", 2);
        option.subtract(3);
        assertThat(option.getQuantity())
            .isEqualTo(0);
    }

    @Test
    void update() {
        var option = new Option("option", 2);
        option.setProduct(product);
        var current = options.save(option);

        current.update("new option", 3);
        var update = options.save(current);
        assertAll(
            () -> assertThat(update.getName())
                .isEqualTo("new option"),
            () -> assertThat(update.getQuantity())
                .isEqualTo(3)
        );
    }

    @Test
    void contains() {
        var optionA = new Option("option A", 1);
        optionA.setProduct(product);
        options.save(optionA);
        products.save(product);
        assertThat(products.findById(1L)
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, product.getName()))
            .contains(optionA))
            .isEqualTo(true);
    }

    @Test
    void AddOption() {
        var productB = new Product(2L, "product B", 5500, "imageUrl", category);
        var optionB = new Option("option B", 2);
        optionB.setProduct(product);
        options.save(optionB);
        productB.add(optionB);
        products.save(productB);

        assertThat(products.findById(2L)
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, 1L))
            .contains(optionB)).isEqualTo(true);
    }

    @Test
    void delete() {
        var option = new Option("option", 1, product);
        var saved = options.save(option);
        options.deleteById(saved.getId());

        assertThat(options.findAll()).isEmpty();
    }
}
