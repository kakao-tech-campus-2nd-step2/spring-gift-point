package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.interceptor.AuthInterceptor;
import gift.model.categories.CategoryDTO;
import gift.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AuthInterceptor authInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String testName1 = "김치";
    private final String testName2 = "선물";
    private final String testName3 = "가구";
    private final String testImgUrl = "url";
    private final Long testId1 = 1L;
    private final Long testId2 = 2L;
    private final Long testId3 = 3L;
    private final CategoryDTO testCategory1 = new CategoryDTO(testId1, testName1, testImgUrl);
    private final CategoryDTO testCategory2 = new CategoryDTO(testId2, testName2, testImgUrl);
    private final CategoryDTO testCategory3 = new CategoryDTO(testId3, testName3, testImgUrl);

    @BeforeEach
    void setUp() throws Exception {
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @DisplayName("카테고리 추가 기능 성공 테스트")
    @Test
    void testInsertCategorySuccess() throws Exception {
        String content = objectMapper.writeValueAsString(testCategory1);
        given(categoryService.insertCategory(any())).willReturn(testCategory1);
        mockMvc.perform(post("/category").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(content().json(content)).andDo(print());
    }

    @DisplayName("카테고리 추가 기능 실패 테스트(중복 이름)")
    @Test
    void testInsertDuplicateCategory() throws Exception {
        String content = objectMapper.writeValueAsString(testCategory1);
        given(categoryService.isDuplicateName(any())).willReturn(true);
        mockMvc.perform(post("/category").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_NAME.getMessage()))
            .andDo(print());
    }

    @DisplayName("카테고리 추가 기능 실패 테스트(이름이 공백)")
    @Test
    void testInsertInvalidCategory() throws Exception {
        String content = objectMapper.writeValueAsString(new CategoryDTO(testId1, "", testImgUrl));
        mockMvc.perform(post("/category").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()))
            .andDo(print());
    }

    @DisplayName("카테고리 목록 조회 기능 테스트")
    @Test
    void testGetCategoryList() throws Exception {
        List<CategoryDTO> list = new ArrayList<>();
        list.add(testCategory1);
        list.add(testCategory2);
        list.add(testCategory3);
        given(categoryService.getCategoryList()).willReturn(list);
        mockMvc.perform(get("/category")).andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("카테고리 수정 기능 성공 테스트")
    @Test
    void testUpdateCategory() throws Exception {
        String content = objectMapper.writeValueAsString(testCategory1);
        given(categoryService.updateCategory(any())).willReturn(testCategory1);
        mockMvc.perform(
                put("/category/" + testId1).contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isOk()).andExpect(content().json(content)).andDo(print());
    }
    @DisplayName("카테고리 수정 기능 실패 테스트(중복 이름)")
    @Test
    void testUpdateDuplicateCategory() throws Exception {
        String content = objectMapper.writeValueAsString(testCategory1);
        given(categoryService.isDuplicateName(any())).willReturn(true);
        mockMvc.perform(post("/category").content(content).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_NAME.getMessage()))
            .andDo(print());
    }
}
