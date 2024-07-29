package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.service.CategoryService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    MemberService memberService;

    @MockBean
    JwtUtil jwtUtil;

    @Test
    void getCategories() throws Exception {
        given(categoryService.getCategories()).willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addCategory() throws Exception {
        String requestBody = """
                {
                    "name": "교환권3",
                    "color": "#6c95d1",
                    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                    "description": ""
                }
        """;

        doNothing().when(categoryService).addCategory(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCategory() throws Exception {
        String requestBody = """
                {
                    "name": "교환권3",
                    "color": "#6c95d1",
                    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                    "description": ""
                }
        """;

        doNothing().when(categoryService).updateCategory(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/1")
        .contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}