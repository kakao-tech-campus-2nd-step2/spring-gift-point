package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.betweenClient.member.MemberDTO;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + jwtUtil.generateToken(new MemberDTO("1234@1234.com", "1234", "basic"));
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