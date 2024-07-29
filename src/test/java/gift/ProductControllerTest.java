package gift;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Spring MockMvc 프레임워크를 사용하여 HTTP 요청 및 응답 테스트

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        Category category = new Category("카테고리테스트", "red", "https://st.kakaocdn.net/category/image.jpg", "교환권 설명");
        categoryRepository.save(category);

        sampleProduct = new Product(null, "아이스 카페 아메리카노 T", 4500L,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", category);
        productRepository.save(sampleProduct);
    }

    @Test
    @DisplayName("상품 조회 테스트")
    void getProductsTest() throws Exception {
        mockMvc.perform(get("/api/products"))  // 'api/products' 경로로 GET 요청
                .andExpect(status().isOk())  // 응답 상태 코드가 '200 OK'인지 검증
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasItem(hasProperty("name", is("아이스 카페 아메리카노 T")))));
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void addProductTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "복숭아 아이스티 T")
                        .param("price", "5900")
                        .param("imageUrl", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg")
                        .param("category.id", String.valueOf(sampleProduct.getCategory().getId()))
                        .param("optionNames", "Large")
                        .param("optionQuantities", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/products"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasItem(hasProperty("name", is("복숭아 아이스티 T")))));
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void editProductTest() throws Exception {

        Product existingProduct = productRepository.findAll().get(0);

        mockMvc.perform(put("/api/products/" + existingProduct.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "아이스 카페 아메리카노 V")
                        .param("price", "3000")
                        .param("imageUrl", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg")
                        .param("category.id", String.valueOf(existingProduct.getCategory().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/products"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasItem(hasProperty("name", is("아이스 카페 아메리카노 V")))));
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteProductTest() throws Exception {

        Product existingProduct = productRepository.findAll().get(0);
        String productName = existingProduct.getName();

        mockMvc.perform(delete("/api/products/" + existingProduct.getId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/products"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", not(hasItem(hasProperty("name", is(productName))))));

    }

    @Test
    @DisplayName("옵션 추가 테스트")
    void addOptionToProductTest() throws Exception {
        Option newOption = new Option();
        newOption.setName("옵션1");
        newOption.setQuantity(100);

        mockMvc.perform(post("/api/products/" + sampleProduct.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOption)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("옵션 추가 완료!")));

        List<Option> options = optionRepository.findAll();
        assert options.size() == 1;
        assert options.get(0).getName().equals("옵션1");
        assert options.get(0).getQuantity().equals(100);
    }

    @Test
    @DisplayName("옵션 추가 시 동일한 이름의 옵션이 존재할 경우 테스트")
    void addOptionWithDuplicateNameTest() throws Exception {
        Option existingOption = new Option();
        existingOption.setName("중복옵션");
        existingOption.setQuantity(100);
        existingOption.setProduct(sampleProduct);
        optionRepository.save(existingOption);

        Option newOption = new Option();
        newOption.setName("중복옵션");
        newOption.setQuantity(200);

        mockMvc.perform(post("/api/products/" + sampleProduct.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOption)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("동일한 상품 내에 동일한 옵션 이름이 존재합니다.")));
    }

}
