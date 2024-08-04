package gift.service;

import gift.entity.product.Product;
import gift.entity.product.ProductDTO;
import gift.entity.user.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WishlistServiceTest {

    private String testEmail;
    private Product product1;
    private Product product2;
    private Product product3;

    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        testEmail = "test@gmail.com";
        userService.signup(new UserDTO(testEmail, "test"));
        product1 = productService.save(new ProductDTO("test1", 123, "test.com", 1L), testEmail);
        product2 = productService.save(new ProductDTO("test2", 123, "test.com", 1L), testEmail);
        product3 = productService.save(new ProductDTO("test3", 123, "test.com", 1L), testEmail);
    }

    @Test
    @DisplayName("삭제를 원하는 product가 내 wishlist에서 삭제되어야 함")
    void wishlistDeleteCascadeProductTest() {
        // given
        wishlistService.addWishlistProduct(testEmail, product1.getId());
        wishlistService.addWishlistProduct(testEmail, product2.getId());

        // when
        wishlistService.deleteWishlist(testEmail, product1.getId());

        // then
        List<Product> products = wishlistService.getWishlistProducts(testEmail);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("다수의 유저가 위시리스트를 만든 경우 테스트")
    void multiUserWishlistTest() {
        // given
        String testEmail1 = "test1@gmail.com";
        String testEmail2 = "test2@gmail.com";

        // when
        // wishlist1
        wishlistService.addWishlistProduct(testEmail1, product1.getId());
        wishlistService.addWishlistProduct(testEmail1, product2.getId());
        // wishlist2
        wishlistService.addWishlistProduct(testEmail2, product2.getId());
        wishlistService.addWishlistProduct(testEmail2, product3.getId());

        List<Product> products1 = wishlistService.getWishlistProducts(testEmail1);
        List<Product> products2 = wishlistService.getWishlistProducts(testEmail2);

        // then
        Assertions.assertAll(
                // wishlist1
                () -> assertThat(products1.get(0).getName()).isEqualTo("test1"),
                () -> assertThat(products1.get(1).getName()).isEqualTo("test2"),
                // wishlist2
                () -> assertThat(products2.get(0).getName()).isEqualTo("test2"),
                () -> assertThat(products2.get(1).getName()).isEqualTo("test3")
        );
    }
}
