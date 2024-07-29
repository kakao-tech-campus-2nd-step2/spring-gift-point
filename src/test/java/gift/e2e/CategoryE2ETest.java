package gift.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CategoryRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.User;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
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
class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TokenService tokenService;

    private String token;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        userRepository.save(new User("test@test.com", "123456"));
        Category category = categoryRepository.save(
            new Category("Category 1", "#FFFFFF", "image1.jpg", "description 1"));
        productRepository.save(new Product("Product 1", 100, "image1.jpg", category));

        categoryId = category.getId();
        token = "Bearer " + tokenService.generateToken("test@test.com");
    }

    @Test
    @DisplayName("save test")
    void addCategoryTest() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("New Category", "#000000",
            "image2.jpg", "New description");

        mockMvc.perform(post("/api/categories")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    @DisplayName("read test")
    void getAllCategoriesTest() throws Exception {
        mockMvc.perform(get("/api/categories")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Category 1"));
    }

    @Test
    @DisplayName("update test")
    void updateCategoryTest() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("Updated Category", "#000000",
            "image2.jpg", "Updated description");

        mockMvc.perform(put("/api/categories/" + categoryId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    @Test
    @DisplayName("delete test")
    void deleteCategoryTest() throws Exception {
        productRepository.deleteAll();

        mockMvc.perform(delete("/api/categories/" + categoryId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("삭제되었습니다."));
    }
}
