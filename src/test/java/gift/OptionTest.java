package gift;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.model.Name;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenOptionIsCreated_thenOptionSavedCorrectly() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        Option savedOption = optionRepository.save(option);

        assertAll(
            () -> assertNotNull(savedOption.getId()),
            () -> assertEquals("TestOption", savedOption.getName().getName()),
            () -> assertEquals(10, savedOption.getQuantity().getQuantity()),
            () -> assertEquals(product, savedOption.getProduct())
        );
    }

    @Test
    void whenAssignProduct_thenProductAssignedCorrectly() {
        Product product1 = new Product(null, new Name("Product1"), 100, "http://example.com/image1.png", 1L, new ArrayList<>());
        productRepository.save(product1);

        Product product2 = new Product(null, new Name("Product2"), 200, "http://example.com/image2.png", 2L, new ArrayList<>());
        productRepository.save(product2);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product1);
        optionRepository.save(option);

        option.assignProduct(product2);
        optionRepository.save(option);

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertEquals(product2, updatedOption.getProduct());
    }

    @Test
    void whenRemoveProduct_thenProductEmpty() {
        // given
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        optionRepository.save(option);

        // when
        option.removeProduct();
        optionRepository.save(option);

        // then
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertNotNull(updatedOption.getProduct());
        assertAll(
            () -> assertNull(updatedOption.getProduct().getId()),
            () -> assertNull(updatedOption.getProduct().getName()),
            () -> assertEquals(0, updatedOption.getProduct().getPrice()),
            () -> assertNull(updatedOption.getProduct().getImageUrl()),
            () -> assertNull(updatedOption.getProduct().getCategoryId()),
            () -> assertTrue(updatedOption.getProduct().getOptions().isEmpty())
        );
    }

    @Test
    void whenUpdateOption_thenOptionUpdatedCorrectly() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        optionRepository.save(option);

        OptionName updatedName = new OptionName("UpdatedOption");
        OptionQuantity updatedQuantity = new OptionQuantity(20);
        option.update(updatedName, updatedQuantity);
        optionRepository.save(option);

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertAll(
            () -> assertEquals("UpdatedOption", updatedOption.getName().getName()),
            () -> assertEquals(20, updatedOption.getQuantity().getQuantity())
        );
    }

    @Test
    void whenDecreaseQuantity_thenQuantityDecreased() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        optionRepository.save(option);

        option.decreaseQuantity(5);
        optionRepository.save(option);

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertEquals(5, updatedOption.getQuantity().getQuantity());
    }


}