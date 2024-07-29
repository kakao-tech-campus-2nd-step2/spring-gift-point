package gift.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OptionQuantityRequest;
import gift.dto.OptionRequest;
import gift.dto.ProductCreateRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
import gift.service.TokenService;
import java.util.ArrayList;
import java.util.List;
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
class OptionE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionService optionService;

    @Autowired
    private TokenService tokenService;

    private String token;
    private Long productId;
    private Long optionId;
    private Long optionId2;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        optionRepository.deleteAll();

        Category category = categoryRepository.save(
            new Category("test-category", "#FFFFFF", "test.jpg", "test description"));
        Product savedProduct = productRepository.save(
            new Product("test", 1000, "test.jpg", category));
        productId = savedProduct.getId();
        Option option = optionService.addOption(productId, new OptionRequest("test-option", 300));
        Option option2 = optionService.addOption(productId, new OptionRequest("test-option2", 500));

        optionId = option.getId();
        optionId2 = option2.getId();
        categoryId = category.getId();
        token = "Bearer " + tokenService.generateToken("test@test.com");
    }

    @Test
    @DisplayName("save test 및 같은 상품에서 동일한 이름의 옵션을 넣으면 오류 방출 테스트")
    void addOptionTest() throws Exception {
        OptionRequest optionRequest = new OptionRequest("test-option3", 700);

        mockMvc.perform(post("/api/products/" + productId + "/options")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("test-option3"));

        mockMvc.perform(post("/api/products/" + productId + "/options")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("상품에 이미 동일한 옵션 이름이 존재합니다: test-option3"));
    }

    @Test
    @DisplayName("read test")
    void getAllOptionsByProductIdTest() throws Exception {
        mockMvc.perform(get("/api/products/" + productId + "/options")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("test-option"))
            .andExpect(jsonPath("$[1].name").value("test-option2"));
    }

    @Test
    @DisplayName("update test 및 같은 상품에서 동일한 이름의 옵션으로 수정을 시도하면 오류 방출 테스트")
    void updateOptionTest() throws Exception {
        OptionRequest optionRequest = new OptionRequest("test-option4", 900);
        OptionRequest sameNameOptionRequest = new OptionRequest("test-option2", 500);

        mockMvc.perform(put("/api/products/" + productId + "/options/" + optionId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionRequest)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/" + productId + "/options")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("test-option4"))
            .andExpect(jsonPath("$[0].quantity").value(900));

        mockMvc.perform(put("/api/products/" + productId + "/options/" + optionId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sameNameOptionRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("상품에 이미 동일한 옵션 이름이 존재합니다: test-option2"));
    }

    @Test
    @DisplayName("delete test 및 남은 옵션이 1개 이하일 때 삭제 방지 테스트")
    void deleteOptionTest() throws Exception {
        mockMvc.perform(delete("/api/products/" + productId + "/options/" + optionId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/api/products/" + productId + "/options/" + optionId2)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("상품의 옵션이 1개 이하인 경우 옵션을 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("옵션 수량 뺴기 테스트 및 뺄셈 후 남은 옵션 수량이 0개 미만일 경우 뺄셈 방지 테스트")
    void subtractOptionQuantityTest() throws Exception {
        OptionQuantityRequest optionQuantityRequest = new OptionQuantityRequest(200);

        mockMvc.perform(put("/api/products/" + productId + "/options/" + optionId + "/sub")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionQuantityRequest)))
            .andExpect(status().isOk());

        mockMvc.perform(put("/api/products/" + productId + "/options/" + optionId + "/sub")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionQuantityRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("옵션에 해당하는 수량이 0개 미만이 될 수 없습니다."));
    }

    @Test
    @DisplayName("상품 생성시 동일한 이름의 옵션을 넣으면 오류 방출 테스트")
    void sameOptionNameTest() throws Exception {
        OptionRequest optionRequest1 = new OptionRequest("test1", 3);
        OptionRequest optionRequest2 = new OptionRequest("test1", 5);
        List<OptionRequest> optionRequests = new ArrayList<>();
        optionRequests.add(optionRequest1);
        optionRequests.add(optionRequest2);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("test", 3000, "test",
            categoryId, optionRequests);

        mockMvc.perform(post("/api/products")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCreateRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].errorMessage").value("옵션 이름이 중복될 수 없습니다."));
    }
}
