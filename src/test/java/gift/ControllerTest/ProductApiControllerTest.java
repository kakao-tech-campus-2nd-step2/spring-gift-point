package gift.ControllerTest;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testAddProduct() throws Exception {
        Category category = new Category("카테고리1");
        categoryRepository.save(category);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"상품1\", \"price\": 1000, \"categoryId\": " + category.getId()
                                + ", \"options\": [ { \"name\": \"옵션1\", \"quantity\": 10 } ] }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("상품1"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.options", hasSize(1)))
                .andExpect(jsonPath("$.options[0].name").value("옵션1"))
                .andExpect(jsonPath("$.options[0].quantity").value(10));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Category category = new Category("카테고리1");
        categoryRepository.save(category);

        Option option1 = new Option("옵션1", 10);
        Product product1 = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option1));
        productRepository.save(product1);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("상품1"))
                .andExpect(jsonPath("$.content[0].price").value(1000))
                .andExpect(jsonPath("$.content[0].options", hasSize(1)))
                .andExpect(jsonPath("$.content[0].options[0].name").value("옵션1"))
                .andExpect(jsonPath("$.content[0].options[0].quantity").value(10));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Category category = new Category("카테고리1");
        categoryRepository.save(category);

        Option option = new Option("옵션1", 10);
        Product product = new Product(
                "상품1",
                1000,
                "이미지URL",
                category,
                Collections.singletonList(option)
        );
        productRepository.save(product);

        mockMvc.perform(put("/api/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"상품2\", \"price\": 2000, \"categoryId\": "
                                + category.getId()
                                + ", \"options\": [ { \"name\": \"옵션2\", \"quantity\": 20 } ] }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("상품2"))
                .andExpect(jsonPath("$.price").value(2000))
                .andExpect(jsonPath("$.options", hasSize(1)))
                .andExpect(jsonPath("$.options[0].name").value("옵션2"))
                .andExpect(jsonPath("$.options[0].quantity").value(20));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Category category = new Category("카테고리1");
        categoryRepository.save(category);

        Option option = new Option("옵션1", 10);
        Product product = new Product(
                "상품1",
                1000,
                "이미지URL",
                category,
                Collections.singletonList(option)
        );
        productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isForbidden());
    }
}
