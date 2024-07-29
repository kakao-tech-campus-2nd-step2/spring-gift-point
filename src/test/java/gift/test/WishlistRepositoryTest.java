package gift.test;

import gift.model.Product;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class WishlistRepositoryTest {
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private SiteUser siteUser;
    private Product product;

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        siteUser = new SiteUser();
        siteUser.setUsername("testuser");
        siteUser.setPassword("testpass");
        siteUser.setEmail("testuser@example.com");
        userRepository.save(siteUser);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(100);
        product.setImageUrl("http://example.com/image.jpg");
        productRepository.save(product);
    }

    @Test
    @DisplayName("상품을 추가하고 유저정보로 불러올때 정상작동")
    public void testFindByUserUsername() {
        // Given
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(siteUser);
        wishlist.setProduct(product);
        wishlist.setQuantity(1);
        wishlistRepository.save(wishlist);

        // When
        List<Wishlist> found = wishlistRepository.findByUserUsername("testuser");

        // Then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getUser().getUsername()).isEqualTo("testuser");
        assertThat(found.get(0).getProduct().getName()).isEqualTo("Test Product");
        assertThat(found.get(0).getQuantity()).isEqualTo(1);
    }
}
