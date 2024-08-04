package gift.util;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.user.UserRequestDTO;
import gift.dto.wishlist.WishlistDTO;
import gift.service.ProductService;
import gift.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductIdValidatorTest {

    private String email;
    private ProductResponseDto product;
    private WishlistDTO wishlist;

    @Autowired
    private Validator validator;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        userService.signup(new UserRequestDTO(email, "test"));
        product = productService.save(new ProductRequestDto("abc", 123, "www.test.com", -1L), email);
        wishlist = new WishlistDTO(product.getId());
    }

    @Test
    public void save_existingProductId() {
        //given
        WishlistDTO wishList = new WishlistDTO(product.getId());

        //when
        Set<ConstraintViolation<WishlistDTO>> violations = validator.validate(wishList);

        //then
        assertThat(violations).isEmpty();
    }

    @Test
    public void save_nonexistentProductId() {
        //given
        WishlistDTO wishList = new WishlistDTO(product.getId() + 1);

        //when
        Set<ConstraintViolation<WishlistDTO>> violations = validator.validate(wishList);

        //then
        assertThat(violations).isNotEmpty();
    }
}
