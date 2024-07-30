package gift.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.exception.category.DuplicateCategoryException;
import gift.exception.category.NotFoundCategoryException;
import gift.model.Category;
import gift.model.Member;
import gift.model.Role;
import gift.request.CategoryUpdateRequest;
import gift.response.CategoryResponse;
import gift.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
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
class CategoryApiControllerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;

    private String token;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken(
            new Member("abc123@a.com", "1234", Role.ROLE_ADMIN));
    }

    @DisplayName("카테고리 생성 요청 테스트")
    @Test
    void add() throws Exception {
        // given
        Category category = new Category("새로운 카테고리");
        String content = objectMapper.writeValueAsString(category);

        given(categoryService.addCategory(any(String.class)))
            .willReturn(new Category(any(String.class)));

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @DisplayName("카테고리 중복 생성 요청 테스트")
    @Test
    void failAdd() throws Exception {
        // given
        Category category = new Category("이미 존재하는 카테고리");
        String content = objectMapper.writeValueAsString(category);

        given(categoryService.addCategory(category.getName()))
            .willThrow(DuplicateCategoryException.class);

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @DisplayName("카테고리 전체 조회 요청 테스트")
    @Test
    void getAll() throws Exception {
        //given
        int dataCounts = 35;
        List<CategoryResponse> categories = new ArrayList<>();
        IntStream.range(0, dataCounts)
            .forEach(i -> {
                Category category = new Category("카테고리 " + i);
                categories.add(CategoryResponse.createCategoryResponse(category));
            });

        given(categoryService.getAllCategories())
            .willReturn(categories);

        //when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/categories")
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(dataCounts)))
            .andDo(print());
    }

    @DisplayName("카테고리 단건 조회 요청 테스트")
    @Test
    void get() throws Exception {
        //given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "찾는 카테고리");
        CategoryResponse categoryResponse = CategoryResponse.createCategoryResponse(category);
        given(categoryService.getCategory(categoryId))
            .willReturn(categoryResponse);

        //when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/categories/" + categoryId)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(equalTo(categoryId.intValue())));
    }

    @DisplayName("존재하지 않는 카테고리 조회 요청 테스트")
    @Test
    void failGet() throws Exception {
        //given
        Long categoryId = 999L;
        given(categoryService.getCategory(categoryId))
            .willThrow(NotFoundCategoryException.class);

        //when
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/categories/" + categoryId)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }

    @DisplayName("카테고리 수정 요청 테스트")
    @Test
    void update() throws Exception {
        //given
        Long savedCategoryId = 1L;
        Category savedCategory = new Category(savedCategoryId, "카테고리");
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest(
            "새로운 카테고리");
        String content = objectMapper.writeValueAsString(updateRequest);
        given(categoryService.updateCategory(savedCategoryId, updateRequest.name()))
            .willReturn(savedCategory);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/categories/{cateogryId}", savedCategoryId)
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
        then(categoryService).should().updateCategory(savedCategoryId, updateRequest.name());
    }

    @DisplayName("존재하지 않는 카테고리 수정 요청 테스트")
    @Test
    void failUpdate() throws Exception {
        //given
        Long notExistedCategoryId = 423L;
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest(
            "새로운 카테고리");
        String content = objectMapper.writeValueAsString(updateRequest);
        given(categoryService.updateCategory(notExistedCategoryId, updateRequest.name())).
            willThrow(NotFoundCategoryException.class);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/categories/{categoryId}", notExistedCategoryId)
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @DisplayName("카테고리 삭제 요청 테스트")
    @Test
    void remove() throws Exception {
        //given
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);
        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/categories")
                    .param("id", categoryId.toString())
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent())
            .andDo(print());
    }


}