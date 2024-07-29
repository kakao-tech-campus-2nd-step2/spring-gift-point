package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.model.Category;
import gift.model.Member;
import gift.model.Options;
import gift.model.Product;
import gift.model.Role;
import gift.request.ProductAddRequest;
import gift.response.OptionResponse;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import gift.service.OptionsService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductApiControllerTest {


    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private OptionsService optionsService;

    private String token;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken(
            new Member("abc123@a.com", "1234", Role.ROLE_ADMIN));
    }

    @DisplayName("모든 옵션과 함께 상품 조회 요청 테스트")
    @Test
    void getProductWithAllOptions() throws Exception {
        //given
        Product product = demoProduct();
        List<OptionResponse> options = new ArrayList<>();
        ProductResponse productResponse = ProductResponse.createProductResponse(product);
        ProductOptionsResponse response = new ProductOptionsResponse(productResponse, options);

        given(productService.getProduct(any(Long.class)))
            .willReturn(product);
        given(optionsService.getAllProductOptions(any(Product.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/{id}/all", product.getId())
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("하나의 옵션과 함께 상품 조회 요청 테스트")
    @Test
    void getProductWithOption() throws Exception {
        //given
        Long optionId = 1L;
        Product product = demoProduct();
        List<OptionResponse> options = new ArrayList<>();
        ProductResponse productResponse = ProductResponse.createProductResponse(product);
        ProductOptionsResponse response = new ProductOptionsResponse(productResponse, options);

        given(productService.getProduct(any(Long.class)))
            .willReturn(product);
        given(optionsService.getProductOption(any(Product.class), any(Long.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/{id}", product.getId())
                    .param("option_id", String.valueOf(optionId))
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("옵션과 함게 상품 생성 요청 테스트")
    @Test
    void add() throws Exception {
        //given
        ProductAddRequest addRequest = demoAddRequest();
        Long optionId = 1L;
        Product product = demoProduct();
        String content = objectMapper.writeValueAsString(addRequest);

        given(productService.addProduct(any(String.class), any(Integer.class),
            any(String.class), any(String.class)))
            .willReturn(product);
        given(optionsService.addOption(any(String.class), any(Integer.class),
            any(Long.class)))
            .willReturn(demoOptions(optionId, product));

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @DisplayName("옵션 없이 상품 생성 요청 테스트")
    @Test
    void failAdd() throws Exception {
        ProductAddRequest addRequest = new ProductAddRequest("상품", 1000, "http://a.com",
            "카테고리", null, null);
        String content = objectMapper.writeValueAsString(addRequest);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());

    }

    @DisplayName("상품 삭제 시 옵션과 함께 삭제 테스트")
    @Test
    void delete() throws Exception {
        //given
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);
        doNothing().when(optionsService).deleteAllOptions(productId);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/products")
                    .param("id", productId.toString())
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());

        then(productService).should().deleteProduct(productId);
        then(optionsService).should().deleteAllOptions(productId);
    }


    private ProductAddRequest demoAddRequest() {
        return new ProductAddRequest("상품", 1000, "http://a.com",
            "카테고리", "옵션", 1);
    }


    private static Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션", 1, product);
    }

    private static Product demoProduct() {
        return new Product(1L, "상품", 1000, "http://a.com", new Category(1L, "카테고리"));
    }


}
