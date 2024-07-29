package gift.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.administrator.category.CategoryApiController;
import gift.administrator.category.CategoryDTO;
import gift.administrator.category.CategoryService;
import gift.error.GlobalExceptionRestController;
import gift.error.NotFoundIdException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CategoryApiControllerTest {

    private final CategoryService categoryService = mock(CategoryService.class);
    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private CategoryApiController categoryApiController;

    @BeforeEach
    void beforeEach() {
        categoryApiController = new CategoryApiController(categoryService);
        mvc = MockMvcBuilders.standaloneSetup(categoryApiController)
            .setControllerAdvice(new GlobalExceptionRestController())
            .defaultResponseCharacterEncoding(UTF_8)
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 카테고리 가져오기")
    void getAllCategories() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        CategoryDTO categoryDTO1 = new CategoryDTO(2L, "인형", "#dd11ff", "image.jpg", "none");
        given(categoryService.getAllCategories()).willReturn(
            Arrays.asList(categoryDTO, categoryDTO1));

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/categories")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(Arrays.asList(categoryDTO, categoryDTO1))));
    }

    @Test
    @DisplayName("아이디에 따른 카테고리 가져오기")
    void getCategoryById() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        given(categoryService.getCategoryById(1L)).willReturn(categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/categories/1")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(categoryDTO)));
    }

    @Test
    @DisplayName("카테고리 저장")
    void addCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        doNothing().when(categoryService).existsByNameThrowException(any());
        given(categoryService.addCategory(any(CategoryDTO.class))).willReturn(categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/api/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(categoryDTO)));
    }

    @Test
    @DisplayName("카테고리 저장시 컬러코드가 valid하지 않은 값 입력")
    void addCategoryNotValid() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#", "image.jpg", "");
        doNothing().when(categoryService).existsByNameThrowException(any());

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/api/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].reason").value("컬러코드가 아닙니다."));
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        doNothing().when(categoryService).existsByNameAndId(anyString(), anyLong());
        given(categoryService.updateCategory(any(CategoryDTO.class), anyLong())).willReturn(categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.put("/api/categories/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(categoryDTO)));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        given(categoryService.getCategoryById(1L)).willReturn(categoryDTO);
        doNothing().when(categoryService).deleteCategory(1L);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.delete("/api/categories/1")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 삭제 존재하지 않는 아이디라 실패해서 NotFoundException 던짐")
    void deleteCategoryFailed() {
        //given
        doThrow(new NotFoundIdException("삭제하려는 카테고리가 존재하지 않습니다.")).when(categoryService).deleteCategory(1L);

        //when

        //then
        assertThatThrownBy(() -> categoryApiController.deleteCategory(1L)).isInstanceOf(
            NotFoundIdException.class).hasMessageContaining("삭제하려는 카테고리가 존재하지 않습니다.");
    }
}
