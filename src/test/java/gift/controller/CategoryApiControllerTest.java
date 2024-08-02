package gift.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
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
import gift.error.ErrorResponse;
import gift.error.ErrorResponse.ErrorField;
import gift.error.GlobalExceptionRestController;
import gift.error.NotFoundIdException;
import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
            .defaultResponseCharacterEncoding(UTF_8).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 카테고리 가져오기")
    void getAllCategories() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        CategoryDTO categoryDTO1 = new CategoryDTO(2L, "인형", "#dd11ff", "image.jpg", "none");
        List<CategoryDTO> categories = Arrays.asList(categoryDTO, categoryDTO1);
        given(categoryService.getAllCategories()).willReturn(categories);
        ApiResponse<List<CategoryDTO>> expectedResponse = new ApiResponse<>(HttpResult.OK,
            "카테고리 전체 조회 성공", HttpStatus.OK, categories);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/categories").contentType("application/json")
                .accept("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("아이디에 따른 카테고리 가져오기")
    void getCategoryById() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        given(categoryService.getCategoryById(1L)).willReturn(categoryDTO);
        ApiResponse<CategoryDTO> expectedResponse = new ApiResponse<>(HttpResult.OK, "카테고리 조회 성공",
            HttpStatus.OK, categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/categories/1").contentType("application/json")
                .accept("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("카테고리 저장")
    void addCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        doNothing().when(categoryService).existsByNameThrowException(any());
        given(categoryService.addCategory(any(CategoryDTO.class))).willReturn(categoryDTO);
        ApiResponse<CategoryDTO> expectedResponse = new ApiResponse<>(HttpResult.OK, "카테고리 추가 성공",
            HttpStatus.CREATED, categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/api/categories").contentType("application/json")
                .accept("application/json").content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("카테고리 저장시 컬러코드가 valid하지 않은 값 입력")
    void addCategoryNotValid() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#", "image.jpg", "");
        doNothing().when(categoryService).existsByNameThrowException(any());
        ApiResponse<ErrorResponse> expectedResponse = new ApiResponse<>(HttpResult.ERROR,
            "잘못된 요청입니다.", HttpStatus.BAD_REQUEST,
            new ErrorResponse(List.of(new ErrorField("color", "#", "컬러코드가 아닙니다."))));

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/api/categories").contentType("application/json")
                .accept("application/json").content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        doNothing().when(categoryService).existsByNameAndId(anyString(), anyLong());
        given(categoryService.updateCategory(any(CategoryDTO.class), anyLong())).willReturn(
            categoryDTO);
        ApiResponse<CategoryDTO> expectedResponse = new ApiResponse<>(HttpResult.OK, "카테고리 수정 성공",
            HttpStatus.OK, categoryDTO);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.put("/api/categories/1").contentType("application/json")
                .content(objectMapper.writeValueAsString(categoryDTO)));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO(1L, "상품권", "#ff11ff", "image.jpg", "");
        given(categoryService.getCategoryById(1L)).willReturn(categoryDTO);
        doNothing().when(categoryService).deleteCategory(1L);
        ApiResponse<CategoryDTO> expectedResponse = new ApiResponse<>(HttpResult.OK, "카테고리 삭제 성공",
            HttpStatus.OK, null);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.delete("/api/categories/1").contentType("application/json")
                .accept("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("카테고리 삭제 존재하지 않는 아이디라 실패해서 NotFoundIdException 던짐")
    void deleteCategoryFailed() throws Exception {
        //given
        doThrow(new NotFoundIdException("삭제하려는 카테고리가 존재하지 않습니다.")).when(categoryService)
            .deleteCategory(1L);
        ApiResponse<String> expectedResponse = new ApiResponse<>(HttpResult.ERROR,
            "아이디 에러", HttpStatus.NOT_FOUND, "삭제하려는 카테고리가 존재하지 않습니다.");

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.delete("/api/categories/1").contentType("application/json")
                .accept("application/json"));

        //then
        resultActions.andExpect(status().isNotFound())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
