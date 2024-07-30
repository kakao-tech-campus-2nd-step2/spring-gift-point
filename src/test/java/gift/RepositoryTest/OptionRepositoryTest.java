package gift.RepositoryTest;

import gift.Entity.Option;
import gift.Entity.Product;
import gift.Repository.OptionJpaRepository;
import gift.Repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OptionRepositoryTest {

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        product1 = new Product(1L, "Product 1", null, 1000, "http://localhost:8080/image1.jpg", false);
        product2 = new Product(2L, "Product 2", null, 2000, "http://localhost:8080/image2.jpg", false);
        productJpaRepository.save(product1);
        productJpaRepository.save(product2);
    }

    @Test
    public void testGetAllOptions() {
        //given
        Option option1 = new Option(1L, product1, "Option1", 1000, 1);
        Option option2 = new Option(2L, product2, "Option2", 2000, 1);
        optionJpaRepository.save(option1);
        optionJpaRepository.save(option2);

        //when
        List<Option> options = optionJpaRepository.findAll();

        //then
        assertNotNull(options);
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionById() {
        //given
        Option option = new Option(1L, product1, "Option1", 1000, 1);
        option = optionJpaRepository.save(option);
        long optionId = option.getId();

        //when
        Optional<Option> foundOption = optionJpaRepository.findById(optionId);

        //then
        assertTrue(foundOption.isPresent());
        assertEquals(option.getName(), foundOption.get().getName());
    }

    @Test
    public void testAddOption() {
        //given
        Option newOption = new Option(2L, product2, "New Option", 2000, 1);

        //when
        Option savedOption = optionJpaRepository.save(newOption);

        //then
        assertNotNull(savedOption);
        assertEquals("New Option", savedOption.getName());
    }

    @Test
    public void testUpdateOption() {
        //given
        Option existingOption = new Option(1L, product1, "Existing Option", 1000, 1);
        existingOption = optionJpaRepository.save(existingOption);
        existingOption.setName("Updated Option");
        existingOption.setPrice(2000);

        //when
        Option updatedOption = optionJpaRepository.save(existingOption);

        //then
        assertNotNull(updatedOption);
        assertEquals("Updated Option", updatedOption.getName());
        assertEquals(2000, updatedOption.getPrice());
    }

    @Test
    public void testSubtractOption() {
        //given
        Option optionDto = new Option(1L, product1, "Existing Option", 1000, 5);
        Option existingOption = new Option(1L, product1, "Existing Option", 1000, 10);
        existingOption = optionJpaRepository.save(existingOption);

        //when
        Option updatedOption = optionJpaRepository.findById(existingOption.getId()).orElseThrow();
        updatedOption.setQuantity(updatedOption.getQuantity() - optionDto.getQuantity());
        Option finalOption = optionJpaRepository.save(updatedOption);

        //then
        assertNotNull(updatedOption);
        assertEquals(5, finalOption.getQuantity());
    }
}