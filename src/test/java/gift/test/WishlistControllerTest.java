package gift.test;

import gift.model.Product;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WishlistControllerTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private SiteUser siteUser;
    private Product product;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
    @DisplayName("담긴 상품 수량 변경할때 정상 작동")
    @WithMockUser(username = "testuser")
    void testUpdateWishlistQuantity() throws Exception {
        // Given
        Wishlist item = new Wishlist();
        item.setUser(siteUser);
        item.setProduct(product);
        item.setQuantity(2);
        wishlistRepository.save(item);

        // When
        mockMvc.perform(post("/web/wishlist/update/" + item.getId())
                .param("quantity", "5"))
            .andExpect(status().isOk());

        // Then
        Wishlist updatedItem = wishlistRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("담긴 상품 삭제할때 정상 작동")
    @WithMockUser(username = "testuser")
    void testRemoveFromWishlist() throws Exception {
        // Given
        Wishlist item = new Wishlist();
        item.setUser(siteUser);
        item.setProduct(product);
        item.setQuantity(2);
        wishlistRepository.save(item);

        // When
        mockMvc.perform(post("/web/wishlist/delete/" + item.getId()))
            .andExpect(status().isOk());

        // Then
        assertThat(wishlistRepository.findById(item.getId())).isEmpty();
    }
}
