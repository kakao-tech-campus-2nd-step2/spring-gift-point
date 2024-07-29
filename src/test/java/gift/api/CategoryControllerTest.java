package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.api.CategoryController;
import gift.product.application.CategoryService;
import gift.product.dto.CategoryRequest;
import gift.product.dto.CategoryResponse;
import gift.product.entity.Category;
import gift.product.util.CategoryMapper;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.CategoryFixture;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private CategoryService categoryService;
    private final String bearerToken = "Bearer token";

    @Test
    @DisplayName("카테고리 전체 조회 기능 테스트")
    void getAllCategories() throws Exception {
        List<CategoryResponse> response = new ArrayList<>();
        Category category1 = CategoryFixture.createCategory("상품권");
        Category category2 = CategoryFixture.createCategory("교환권");
        response.add(CategoryMapper.toResponseDto(category1));
        response.add(CategoryMapper.toResponseDto(category2));

        String responseJson = objectMapper.writeValueAsString(response);
        given(categoryService.getAllCategories())
                .willReturn(response);

        mockMvc.perform(get("/api/categories")
                .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(categoryService).getAllCategories();
    }

    @Test
    @DisplayName("카테고리 상세 조회 기능 테스트")
    void getCategory() throws Exception {
        CategoryResponse response = CategoryMapper.toResponseDto(
                CategoryFixture.createCategory("상품권")
        );
        Long responseId = 1L;
        String responseJson = objectMapper.writeValueAsString(response);
        given(categoryService.getCategoryByIdOrThrow(any()))
                .willReturn(response);

        mockMvc.perform(get("/api/categories/{id}", responseId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(categoryService).getCategoryByIdOrThrow(responseId);
    }

    @Test
    @DisplayName("카테고리 상세 조회 실패 테스트")
    void getCategoryFailed() throws Exception {
        Long categoryId = 1L;
        Throwable exception = new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        given(categoryService.getCategoryByIdOrThrow(any()))
                .willThrow(exception);

        mockMvc.perform(get("/api/categories/{id}", categoryId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(exception.getMessage()));

        verify(categoryService).getCategoryByIdOrThrow(categoryId);
    }

    @Test
    @DisplayName("카테고리 추가 기능 테스트")
    void addCategory() throws Exception {
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );
        CategoryResponse response = CategoryMapper.toResponseDto(
                CategoryMapper.toEntity(request)
        );
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);
        given(categoryService.createCategory(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(categoryService).createCategory(request);
    }

    @Test
    @DisplayName("카테고리 삭제 기능 테스트")
    void deleteCategory() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/api/categories/{id}", categoryId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(categoryService).deleteCategoryById(categoryId);
    }

    @Test
    @DisplayName("카테고리 수정 기능 테스트")
    void updateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryRequest request = new CategoryRequest(
                "상품권",
                "#ffffff",
                "https://product-shop.com",
                ""
        );
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/categories/{id}", categoryId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(categoryService).updateCategory(categoryId, request);
    }

}