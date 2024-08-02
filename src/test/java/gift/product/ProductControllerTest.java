package gift.product;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.ProductController;
import gift.domain.Member.MemberRequest;
import gift.domain.Product.ProductRequest;
import gift.domain.Product.ProductResponse;
import gift.domain.Token;
import gift.service.CategoryService;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.util.JwtUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private CategoryService categoryService;
    @MockBean MemberService memberService;
    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        MemberRequest member = new MemberRequest("admin2@kakao.com", "2222");
        Token mockToken = new Token("mocktoken");

        given(memberService.login(member)).willReturn(mockToken);
        token = mockToken.getToken();

        given(jwtUtil.isTokenValid(token)).willReturn(true);

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("상품 단일 조회 테스트")
    public void testGetProductById() throws Exception {
        //given
        ProductResponse response = new ProductResponse(1L,"우유", 1000L, "https://milk.jpg", 1L, List.of(1L, 2L));
        given(productService.getProductById(1L)).willReturn(response);

        //when
        mockMvc.perform(get("/api/products/{id}",1L)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(productService).should(times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void testAddProduct() throws Exception {
        // given
        ProductRequest request = new ProductRequest("빵", 2000L, "https://bread.jpg", 1L, List.of(2L, 3L));
        // when
        mockMvc.perform(post("/api/products")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        then(productService).should(times(1)).addProduct(request);
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    void testUpdateProduct() throws Exception {
        // given
        ProductRequest request = new ProductRequest("새빵", 2222L, "https://bread.jpg", 1L, List.of(2L, 3L));
        Long productId = 2L;
        // when
        mockMvc.perform(put("/api/products/{id}", productId)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        then(productService).should(times(1)).updateProduct(productId, request);
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void testDeleteProduct() throws Exception {
        // given
        Long productId = 3L;
        // when
        mockMvc.perform(delete("/api/products/{id}", productId)
                .headers(getHttpHeaders()))
            .andExpect(status().isOk());

        then(productService).should(times(1)).deleteProduct(productId);
    }

}
