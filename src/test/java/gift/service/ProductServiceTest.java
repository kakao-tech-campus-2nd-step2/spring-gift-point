package gift.service;

import gift.entity.category.Category;
import gift.entity.category.CategoryRequest;
import gift.entity.product.Product;
import gift.entity.product.ProductDTO;
import gift.entity.user.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private CategoryService categoryService;

    String testEmail = "test@gmail.com";

    @Test
    @DisplayName("product가 삭제되었을 때 product_wishlist에서 해당 행이 삭제되어야 함")
    void productDeleteCascadeWishlistTest() {
        // given
        userService.signup(new UserDTO(testEmail, "test"));
        Product product = productService.save(new ProductDTO("test", 123, "test.com", 1L), testEmail);
        wishlistService.addWishlistProduct(testEmail, product.getId());

        // when
        productService.delete(product.getId(), testEmail);

        // then
        List<Product> products = wishlistService.getWishlistProducts(testEmail);
        assertThat(products).hasSize(0);
    }

    @Test
    @DisplayName("카테고리별 상품들만 보여줘야 함")
    void findProductsByCategoryTest() {
        // given
        userService.signup(new UserDTO(testEmail, "test"));

        Category category = categoryService.save(new CategoryRequest("test", "#test", "", ""));

        productService.save(new ProductDTO("test1", 111, "test1.com", 1L), testEmail);
        productService.save(new ProductDTO("test2", 222, "test2.com", category.getId()), testEmail);
        productService.save(new ProductDTO("test3", 333, "test3.com", category.getId()), testEmail);

        // when
        Page<Product> productsByCategory = productService.getProductsByCategory(category.getId(), PageRequest.of(0, 10));

        // then
        assertThat(productsByCategory.getTotalElements()).isEqualTo(2);
    }
}
