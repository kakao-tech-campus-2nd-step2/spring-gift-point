package gift.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import gift.controller.CategoryController;
import gift.controller.MemberController;
import gift.domain.Category;
import gift.domain.Category.CategoryRequest;
import gift.domain.Category.CategoryResponse;
import gift.domain.Member;
import gift.domain.Member.MemberRequest;
import gift.domain.Token;
import gift.service.CategoryService;
import gift.service.MemberService;
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
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private MemberService memberService;
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
    @DisplayName("카테고리 전체 조회 기능 테스트")
    void testGetAllCategories() throws Exception {
        //given
        List<CategoryResponse> responses = new ArrayList<>();

        CategoryResponse category1 = new CategoryResponse(1L,"식품", "#3EE715", "https://category1.jpg", "식품 카테고리");
        CategoryResponse category2 = new CategoryResponse(2L,"도서", "#8A691C", "https://category2.jpg", "도서 카테고리");

        responses.add(category1);
        responses.add(category2);

        given(categoryService.getAllCategories()).willReturn(responses);

        mockMvc.perform(get("/api/category")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("식품"))
                .andExpect(jsonPath("$[1].name").value("도서"))
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));

        then(categoryService).should(times(1)).getAllCategories();
    }

    @Test
    @DisplayName("카테고리 단일 조회 테스트")
    public void testGetCategoryById() throws Exception {
        //given
        CategoryResponse response = new CategoryResponse(1L, "식품", "#3EE715", "https://category1.jpg", "식품 카테고리");
        given(categoryService.getCategoryById(1L)).willReturn(response);

        //when
        mockMvc.perform(get("/api/category/{id}",1L)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(categoryService).should(times(1)).getCategoryById(1L);
    }

    @Test
    @DisplayName("카테고리 추가 테스트")
    void testAddCategory() throws Exception {
        // given
        CategoryRequest request = new CategoryRequest("상품권","#ffffff","http://gift.jpg","상품권 카테고리");

        // when
        mockMvc.perform(post("/api/category")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(categoryService).should(times(1)).addCategory(request);
    }

    @Test
    @DisplayName("카테고리 업데이트 테스트")
    void testUpdateCategory() throws Exception {
        // given
        Long categoryId = 3L;
        CategoryRequest request = new CategoryRequest("업데이트된 상품권", "#FFFFFF", "https://updated.jpg", "상품권 카테고리");

        // when
        mockMvc.perform(put("/api/category/{id}", categoryId)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        then(categoryService).should(times(1)).updateCategory(categoryId, request);
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void testDeleteCategory() throws Exception {
        // given
        Long categoryId = 3L;

        // when
        mockMvc.perform(delete("/api/category/{id}", categoryId)
                .headers(getHttpHeaders()))
                .andExpect(status().isOk());

        then(categoryService).should(times(1)).deleteCategory(categoryId);
    }

}
