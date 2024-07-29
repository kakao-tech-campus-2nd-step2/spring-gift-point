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
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenSaveOption_thenOptionIsSavedCorrectly() {
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
    void givenProductId_whenFindByProductId_thenReturnOptions() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option1 = new Option(null, new OptionName("TestOption1"), new OptionQuantity(10), product);
        Option option2 = new Option(null, new OptionName("TestOption2"), new OptionQuantity(20), product);
        optionRepository.saveAll(Arrays.asList(option1, option2));

        List<Option> options = optionRepository.findByProductId(product.getId());

        assertAll(
            () -> assertEquals(2, options.size()),
            () -> assertEquals("TestOption1", options.get(0).getName().getName()),
            () -> assertEquals("TestOption2", options.get(1).getName().getName())
        );
    }

    @Test
    void whenUpdateOption_thenChangesArePersisted() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        Option savedOption = optionRepository.save(option);

        savedOption.update(new OptionName("UpdatedOption"), new OptionQuantity(20));
        Option updatedOption = optionRepository.save(savedOption);

        assertAll(
            () -> assertEquals("UpdatedOption", updatedOption.getName().getName()),
            () -> assertEquals(20, updatedOption.getQuantity().getQuantity())
        );
    }

    @Test
    void whenDecreaseQuantity_thenQuantityIsDecreased() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        Option savedOption = optionRepository.save(option);

        savedOption.decreaseQuantity(5);
        Option updatedOption = optionRepository.save(savedOption);

        assertEquals(5, updatedOption.getQuantity().getQuantity());
    }

    @Test
    void whenDecreaseQuantityBelowZero_thenQuantityIsZero() {
        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        Option option = new Option(null, new OptionName("TestOption"), new OptionQuantity(10), product);
        Option savedOption = optionRepository.save(option);

        savedOption.decreaseQuantity(15);
        Option updatedOption = optionRepository.save(savedOption);

        assertEquals(1, updatedOption.getQuantity().getQuantity());
    }

}