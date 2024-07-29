package gift.service;

import gift.entity.Product;
import gift.entity.ProductDTO;
import gift.entity.UserDTO;
import gift.entity.WishlistDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private WishlistService wishlistService;

    @Test
    @DisplayName("product가 삭제되었을 때 product_wishlist에서 해당 행이 삭제되어야 함")
    void productDeleteCascadeWishlistTest() {
        // given
        String testEmail = "test@gmail.com";
        userService.signup(new UserDTO(testEmail, "test"));
        Product product = productService.save(new ProductDTO("test", 123, "test.com", 1L), testEmail);
        wishlistService.addWishlistProduct(testEmail, new WishlistDTO(product.getId()));

        // when
        productService.delete(product.getId(), testEmail);

        // then
        List<Product> products = wishlistService.getWishlistProducts(testEmail);
        assertThat(products).hasSize(0);
    }
}
