package gift;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.model.Name;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    void whenProductIsCreated_thenProductSavedCorrectly() {
        Name name = new Name("TestProduct");
        List<Option> options = new ArrayList<>();
        Product product = new Product(null, name, 100, "http://example.com/image.png", 1L, options);
        Product savedProduct = productRepository.save(product);

        assertAll(
            () -> assertNotNull(savedProduct.getId()),
            () -> assertEquals("TestProduct", savedProduct.getName().getName()),
            () -> assertEquals(100, savedProduct.getPrice()),
            () -> assertEquals("http://example.com/image.png", savedProduct.getImageUrl()),
            () -> assertEquals(1L, savedProduct.getCategoryId())
        );
    }

    @Test
    void whenAddOption_thenOptionAdded() {
        Name name = new Name("TestProduct");
        List<Option> options = new ArrayList<>();
        Product product = new Product(null, name, 100, "http://example.com/image.png", 1L, options);
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        product.addOption(option);
        optionRepository.save(option);

        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(1, savedProduct.getOptions().size());
        assertEquals("TestOption", savedProduct.getOptions().get(0).getName().getName());
    }

    @Test
    void whenClearOptions_thenOptionsCleared() {
        Name name = new Name("TestProduct");
        List<Option> options = new ArrayList<>();
        Product product = new Product(null, name, 100, "http://example.com/image.png", 1L, options);
        productRepository.save(product);

        Option option1 = new Option(null, new OptionName("TestOption1"), new OptionQuantity(10), product);
        Option option2 = new Option(null, new OptionName("TestOption2"), new OptionQuantity(20), product);
        product.addOption(option1);
        product.addOption(option2);
        optionRepository.saveAll(List.of(option1, option2));

        product.clearOptions();
        productRepository.save(product);

        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(0, savedProduct.getOptions().size());
    }
}