package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.betweenClient.category.CategoryDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.product.OneProductResponseDTO;
import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = ProductController.class)
@Import(JwtUtil.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    @Mock
    CategoryDTO categoryDTO;

    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + jwtUtil.generateToken(new MemberDTO("1234@1234.com", "1234", "basic"));
    }

    @Test
    void getOneProduct() throws Exception {
        given(productService.getProductWithOptions(any())).willReturn(new OneProductResponseDTO(1L, "a", 123, "asdfasdf", new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1").contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void addProduct() throws Exception {
        String requestJson = """
                    {
                        "name": "productName",
                        "price": 1234,
                        "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                        "categoryName" : "categoryName",
                        "optionName" : "옵션",
                        "optionQuantity" : 1234
                    }
                """;

        doNothing().when(productService).addProduct(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").header("Authorization", token).content(requestJson).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1").header("Authorization", token).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateProduct() throws Exception {
        String requestJson = """
                    {
                        "name": "productName",
                        "price": 1234,
                        "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                        "categoryName" : "categoryName"
                    }
                """;

        doNothing().when(productService).updateProduct(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1").header("Authorization", token).content(requestJson
        ).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}