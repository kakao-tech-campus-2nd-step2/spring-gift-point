//package gift.ControllerTest;
//
//import gift.model.Category;
//import gift.model.Option;
//import gift.model.Product;
//import gift.repository.CategoryRepository;
//import gift.repository.OptionRepository;
//import gift.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class OptionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private OptionRepository optionRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void setup() {
//        optionRepository.deleteAll();
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//    }
//
//    @Test
//    void testAddOption() throws Exception {
//        Category category = new Category("카테고리1");
//        categoryRepository.save(category);
//
//        Option option = new Option("옵션1", 10);
//        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
//        productRepository.save(product);
//
//        mockMvc.perform(post("/api/products/" + product.getId() + "/options")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"name\": \"옵션2\", \"quantity\": 20 }"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("옵션2"))
//                .andExpect(jsonPath("$.quantity").value(20));
//    }
//
//    @Test
//    void testGetOptionsByProductId() throws Exception {
//        Category category = new Category("카테고리1");
//        categoryRepository.save(category);
//
//        Option option1 = new Option("옵션1", 10);
//        Option option2 = new Option("옵션2", 20);
//        Product product = new Product("상품1", 1000, "이미지URL", category, Arrays.asList(option1, option2));
//        productRepository.save(product);
//
//        mockMvc.perform(get("/api/products/" + product.getId() + "/options"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name").value("옵션1"))
//                .andExpect(jsonPath("$[0].quantity").value(10))
//                .andExpect(jsonPath("$[1].name").value("옵션2"))
//                .andExpect(jsonPath("$[1].quantity").value(20));
//    }
//
//    @Test
//    void testUpdateOption() throws Exception {
//        Category category = new Category("카테고리1");
//        categoryRepository.save(category);
//
//        Option option = new Option("옵션1", 10);
//        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
//        productRepository.save(product);
//
//        mockMvc.perform(put("/api/products/" + product.getId() + "/options/" + option.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"name\": \"옵션2\", \"quantity\": 20 }"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("옵션2"))
//                .andExpect(jsonPath("$.quantity").value(20));
//    }
//
//    @Test
//    void testDeleteTwoOption() throws Exception {
//        Category category = new Category("카테고리1");
//        categoryRepository.save(category);
//
//        Option option1 = new Option("옵션1", 10);
//        Option option2 = new Option("옵션2", 20);
//        Product product = new Product("상품1", 1000, "이미지URL", category, Arrays.asList(option1, option2));
//        productRepository.save(product);
//
//        mockMvc.perform(delete("/api/products/" + product.getId() + "/options/" + option1.getId()))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(get("/api/products/" + product.getId() + "/options"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//
//    @Test
//    void testDeleteOneOption() throws Exception {
//        Category category = new Category("카테고리1");
//        categoryRepository.save(category);
//
//        Option option = new Option("옵션1", 10);
//        Product product = new Product("상품1", 1000, "이미지URL", category, Collections.singletonList(option));
//        productRepository.save(product);
//
//        mockMvc.perform(delete("/api/products/" + product.getId() + "/options/" + option.getId()))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/api/products/" + product.getId() + "/options"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//    }
//}
