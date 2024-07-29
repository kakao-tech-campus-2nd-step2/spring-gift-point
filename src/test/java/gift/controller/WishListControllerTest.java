package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.betweenClient.member.MemberDTO;
import gift.service.MemberService;
import gift.service.WishListService;
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

@WebMvcTest(controllers = WishListController.class)
@Import(JwtUtil.class)
class WishListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private WishListService wishListService;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = "Bearer " + jwtUtil.generateToken(new MemberDTO("1234@1234.com", "1234", "basic"));
    }


    @Test
    void addWishes() throws Exception {
        String requestJson = """
                {
                    "id" : "1",
                    "name": "제품",
                    "price": 1234,
                    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                    "categoryName" : "기타"
                }
                """;

        doNothing().when(wishListService).addWishes(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/wishes").header("Authorization", token).content(requestJson).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void deleteWishes() throws Exception {
        doNothing().when(wishListService).removeWishListProduct(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/wishes/1").header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void setWishes() throws Exception {
        String requestJson = """
                {
                    "id" : "1",
                    "name": "제품",
                    "price": 1234,
                    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                    "categoryName" : "기타"
                }
                """;

        doNothing().when(wishListService).setWishListNumber(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/wishes/10").header("Authorization", token).content(requestJson
        ).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}