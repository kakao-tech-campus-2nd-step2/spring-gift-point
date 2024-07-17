package gift.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gift.product.model.ProductRepository;
import gift.product.model.dto.Product;
import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import gift.wishlist.model.WishListRepository;
import gift.wishlist.model.dto.Wish;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;
    private AppUser appUser;
    private Product product;
    private Wish wish;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        product = new Product("Test", 1000, "url", appUser);
        wish = new Wish(appUser, product, 5);
        appUser = userRepository.save(appUser);
        product = productRepository.save(product);
        wishListRepository.save(wish);

    }

    @Test
    public void testFindActiveProductById() {
        Product foundProduct = productRepository.findByIdAndIsActiveTrue(product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product"));
        assertEquals(product.getName(), foundProduct.getName());
    }

    @Test
    public void testSaveAndDelete() {
        Product savedProduct = productRepository.save(product);

        productRepository.delete(savedProduct);
        Optional<Product> result = productRepository.findById(savedProduct.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindProductWithWishCount() {
        Optional<Tuple> optionalResult = productRepository.findProductByIdWithWishCount(product.getId());

        optionalResult.isPresent();
        Tuple result = optionalResult.get();

        Product foundProduct = result.get("product", Product.class);
        Long wishCount = result.get("wishCount", Long.class);

        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(1L, wishCount);
    }
}
