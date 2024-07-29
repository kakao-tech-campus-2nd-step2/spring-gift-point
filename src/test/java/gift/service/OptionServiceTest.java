package gift.service;

import gift.entity.*;
import gift.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OptionServiceTest {

    private String email;
    private User user;
    private Option option;

    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        String password = "test";
        userService.signup(new UserDTO(email, password));
        user = userService.findOne(email);

        option = optionService.save(new OptionDTO("abc", 10), email);
    }

    @Test
    void findById() {
        // given
        // when
        Option expect = optionService.findById(option.getId());

        // then
        assertAll(
                () -> assertThat(option.getName()).isEqualTo(expect.getName()),
                () -> assertThat(option.getQuantity()).isEqualTo(expect.getQuantity())
        );
    }

    @Test
    void update() {
        // given
        OptionDTO update = new OptionDTO("def", 456);

        // when
        Option expect = optionService.update(option.getId(), update, email);

        // then
        assertAll(
                () -> assertThat(expect.getName()).isEqualTo(update.getName()),
                () -> assertThat(expect.getQuantity()).isEqualTo(update.getQuantity())
        );
    }

    @Test
    void delete() {
        // given
        // when
        optionService.delete(option.getId(), email);
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> optionService.findById(option.getId()));

        // then
        assertThat(exception.getMessage())
                .isEqualTo("Option not found with id: " + option.getId());
        assertThat(userService.findOne(email)).isNotNull();
    }

    @Test
    void subtract_total_보다_amount가_더_많은_테스트() {
        // given
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> optionService.subtract(option.getId(), 9999));

        // then
        assertThat(exception.getMessage()).isEqualTo("Not enough quantity");
    }

    @Test
    void amount가_0_이하인_테스트() {
        // given
        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> optionService.subtract(option.getId(), -1));

        // then
        assertThrows(IllegalArgumentException.class,
                () -> optionService.subtract(option.getId(), 0));
        assertThat(exception.getMessage()).isEqualTo("Invalid quantity");
    }

    @Test
    void subtract_성공() {
        // given
        int total = 2834;
        int amount = 349;

        option.setQuantity(total);

        // when
        optionService.subtract(option.getId(), amount);

        // then
        assertThat(option.getQuantity()).isEqualTo(total - amount);
    }

    @Test
    void 같은_이름의_option이_product에_있을_때() {
        // given
        Product product = productService.save(new ProductDTO("test", 123, "test.com", 1L), email);
        Option sameNameOption = optionService.save(new OptionDTO("abc", 456), email);

        // when
        productService.addProductOption(product.getId(), option.getId(), email);

        // then
        assertThrows(DataIntegrityViolationException.class,
                () -> productService.addProductOption(product.getId(), sameNameOption.getId(), email));
    }
}
