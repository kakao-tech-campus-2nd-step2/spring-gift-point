package gift.unitTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.WebConfig;
import gift.controller.category.CategoryController;
import gift.controller.category.CategoryRequest;
import gift.service.CategoryService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private WebConfig webConfig;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("카테고리 목록 조회 - 성공")
    @Test
    void getAllCategoriesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")).andExpect(status().isOk());
        verify(categoryService).findAll(any(Pageable.class));
    }

    @DisplayName("카테고리 생성 - 성공")
    @Test
    void createCategoryTest() throws Exception {
        // given
        var request = new CategoryRequest("validName", "validColor", "validDescription",
            "validUrl");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

            // then
            .andExpect(status().isCreated());
        verify(categoryService).save(any(CategoryRequest.class));
    }

    @DisplayName("카테고리 수정 - 성공")
    @Test
    void updateCategoryTest() throws Exception {
        // given
        var request = new CategoryRequest("validName", "validColor", "validDescription",
            "validUrl");

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

            // then
            .andExpect(status().isOk());
        verify(categoryService).update(any(UUID.class), any(CategoryRequest.class));
    }

    @DisplayName("카테고리 삭제 - 성공")
    @Test
    void deleteCategoryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + UUID.randomUUID()))
            .andExpect(status().isNoContent());
        verify(categoryService).delete(any(UUID.class));
    }
}
