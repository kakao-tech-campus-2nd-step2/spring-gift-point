package gift.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.category.CategoryCreateRequest;
import gift.dto.category.CategoryResponse;
import gift.dto.category.CategoryUpdateRequest;
import gift.exception.category.CategoryNotFoundException;
import gift.service.CategoryService;
import gift.util.TokenValidator;
import gift.util.constants.CategoryConstants;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TokenValidator tokenValidator;

    private CategoryResponse categoryResponse;

    @BeforeEach
    public void setUp() {
        categoryResponse = new CategoryResponse(
            1L,
            "Category",
            "#000000",
            "imageUrl",
            "description"
        );
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    public void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(categoryResponse));

        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Category"));
    }

    @Test
    @DisplayName("카테고리 ID로 조회")
    public void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponse);

        mockMvc.perform(get("/api/categories/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Category"));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 조회")
    public void testGetCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenThrow(
            new CategoryNotFoundException(CategoryConstants.CATEGORY_NOT_FOUND + 1));

        mockMvc.perform(get("/api/categories/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(CategoryConstants.CATEGORY_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("카테고리 추가")
    public void testAddCategory() throws Exception {
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(
            "Category",
            "#000000",
            "imageUrl",
            "description"
        );
        when(categoryService.addCategory(categoryCreateRequest)).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Category\", \"color\": \"#000000\", \"imageUrl\": \"imageUrl\", \"description\": \"description\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Category"));
    }

    @Test
    @DisplayName("카테고리 수정")
    public void testUpdateCategory() throws Exception {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Updated Category",
            "#FFFFFF",
            "newImageUrl",
            "newDescription"
        );
        CategoryResponse updatedCategoryResponse = new CategoryResponse(
            1L,
            "Updated Category",
            "#FFFFFF",
            "newImageUrl",
            "newDescription"
        );
        when(categoryService.updateCategory(1L, categoryUpdateRequest)).thenReturn(
            updatedCategoryResponse);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Updated Category\", \"color\": \"#FFFFFF\", \"imageUrl\": \"newImageUrl\", \"description\": \"newDescription\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Category"))
            .andExpect(jsonPath("$.color").value("#FFFFFF"))
            .andExpect(jsonPath("$.imageUrl").value("newImageUrl"))
            .andExpect(jsonPath("$.description").value("newDescription"));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 수정")
    public void testUpdateCategoryNotFound() throws Exception {
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(
            "Updated Category",
            "#FFFFFF",
            "newImageUrl",
            "newDescription"
        );
        when(categoryService.updateCategory(1L, categoryUpdateRequest)).thenThrow(
            new CategoryNotFoundException(CategoryConstants.CATEGORY_NOT_FOUND + 1));

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Updated Category\", \"color\": \"#FFFFFF\", \"imageUrl\": \"newImageUrl\", \"description\": \"newDescription\"}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(CategoryConstants.CATEGORY_NOT_FOUND + 1));
    }
}
