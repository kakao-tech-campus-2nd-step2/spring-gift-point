package gift.ControllerTest;

import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testAddCategory() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"카테고리1\", \"color\": \"#FFFFFF\", \"imageUrl\": \"이미지URL\", \"description\": \"설명\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("카테고리1"))
                .andExpect(jsonPath("$.color").value("#FFFFFF"))
                .andExpect(jsonPath("$.imageUrl").value("이미지URL"))
                .andExpect(jsonPath("$.description").value("설명"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category1 = new Category("카테고리1", "#FFFFFF", "이미지URL1", "설명1");
        Category category2 = new Category("카테고리2", "#000000", "이미지URL2", "설명2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("카테고리1"))
                .andExpect(jsonPath("$[0].color").value("#FFFFFF"))
                .andExpect(jsonPath("$[0].imageUrl").value("이미지URL1"))
                .andExpect(jsonPath("$[0].description").value("설명1"))
                .andExpect(jsonPath("$[1].name").value("카테고리2"))
                .andExpect(jsonPath("$[1].color").value("#000000"))
                .andExpect(jsonPath("$[1].imageUrl").value("이미지URL2"))
                .andExpect(jsonPath("$[1].description").value("설명2"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = new Category("카테고리1", "#FFFFFF", "이미지URL", "설명");
        categoryRepository.save(category);

        mockMvc.perform(put("/api/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"카테고리2\", \"color\": \"#000000\", \"imageUrl\": \"이미지URL2\", \"description\": \"설명2\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("카테고리2"))
                .andExpect(jsonPath("$.color").value("#000000"))
                .andExpect(jsonPath("$.imageUrl").value("이미지URL2"))
                .andExpect(jsonPath("$.description").value("설명2"));
    }
}
