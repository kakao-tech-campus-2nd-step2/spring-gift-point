package gift.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import gift.controller.CategoryController;
import gift.controller.MemberController;
import gift.domain.Category;
import gift.domain.Member;
import gift.service.CategoryService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("카테고리 전체 조회 기능 테스트")
    void testGetAllCategories() throws Exception {
        //given
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category("식품", "#3EE715", "https://category1.jpg", "식품 카테고리");
        Category category2 = new Category("도서", "#8A691C", "https://category2.jpg", "도서 카테고리");

        categories.add(category1);
        categories.add(category2);

        String jsonResponse = objectMapper.writeValueAsString(categories);

        given(categoryService.getAllCategories()).willReturn(categories);

        //when
        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(categoryService).should(times(1)).getAllCategories();
    }

    @Test
    @DisplayName("카테고리 단일 조회 테스트")
    public void testGetCategoryById() throws Exception {
        //given
        Category category = new Category(1L, "식품", "#3EE715", "https://category1.jpg", "식품 카테고리");
        given(categoryService.getCategoryById(1L)).willReturn(category);

        String jsonResponse = objectMapper.writeValueAsString(category);

        //when
        mockMvc.perform(get("/api/categories/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        then(categoryService).should(times(1)).getCategoryById(1L);
    }


}
