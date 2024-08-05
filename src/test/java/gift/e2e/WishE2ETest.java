package gift.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.WishRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wish;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WishE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TokenService tokenService;

    private String token;
    private Long productId;
    private Long wishId;

    @BeforeEach
    void setUp() {
        wishRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = categoryRepository.save(
            new Category("test-category", "#FFFFFF", "test.jpg", "test description"));
        User savedUser = userRepository.save(new User("test@test.com", "123456"));
        Product savedProduct = productRepository.save(
            new Product("test", 1000, "test.jpg", category));
        Wish savedWish = wishRepository.save(new Wish(savedUser, savedProduct));

        productId = savedProduct.getId();
        wishId = savedWish.getId();
        token = "Bearer " + tokenService.generateToken("test@test.com");
    }

    @Test
    @DisplayName("save test")
    void addWishTest() throws Exception {
        WishRequest wishRequest = new WishRequest(productId);

        mockMvc.perform(post("/api/wishes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("read test")
    void getAllWishesTest() throws Exception {
        mockMvc.perform(get("/api/wishes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete test")
    void deleteWishTest() throws Exception {
        mockMvc.perform(delete("/api/wishes/" + wishId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("삭제되었습니다."));
    }
}
